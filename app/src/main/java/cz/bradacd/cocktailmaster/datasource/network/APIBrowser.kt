package cz.bradacd.cocktailmaster.datasource.network

import android.widget.ImageView
import androidx.core.net.toUri
import coil.load
import cz.bradacd.cocktailmaster.R
import cz.bradacd.cocktailmaster.common.DrinkCategory
import cz.bradacd.cocktailmaster.common.NotEnoughParametersException
import cz.bradacd.cocktailmaster.datasource.browser.Browser
import cz.bradacd.cocktailmaster.datasource.displayable.DisplayableDrink
import cz.bradacd.cocktailmaster.datasource.displayable.DisplayableDrinkDetail
import cz.bradacd.cocktailmaster.datasource.displayable.DisplayableIngredient
import cz.bradacd.cocktailmaster.datasource.network.mapping.toDisplayable
import kotlinx.coroutines.*

// TODO otestovat jak funguje ensureActive
class CocktailAPIBrowser() : Browser {
    override val sourceTag = "cocktailAPI"

    private suspend fun getDrinksByName(name: String): List<DisplayableDrinkDetail> =
        CocktailApi.fetchDrinksByName(name).map { it.toDisplayable(sourceTag) }

    private suspend fun getDrinksByCategory(cat: DrinkCategory): List<DisplayableDrink> =
        CocktailApi.fetchIngredientsByCategory(cat).map { it.toDisplayable(sourceTag) }

    private suspend fun getDrinksByIngredient(ingredientName: String): List<DisplayableDrink> =
        CocktailApi.fetchDrinksByIngredient(ingredientName).map { it.toDisplayable(sourceTag) }

    override suspend fun getIngredientsByName(name: String): List<DisplayableIngredient> =
        CocktailApi.fetchIngredientsByName(name).map { it.toDisplayable(sourceTag) }

    override suspend fun getDrinkDetail(id: String): DisplayableDrinkDetail? =
        CocktailApi.fetchDrinkDetailById(id)?.toDisplayable(sourceTag)

    // TODO otestovat si coroutiny ve stylu výpis vláken, delay, atd..
    // TODO Ingredience "Peach" padá na EOF
    override suspend fun getDrinksMultipleParams(
        name: String?,
        category: DrinkCategory?,
        ingredients: Array<String>?,
        loadImages: Boolean
    ): List<DisplayableDrink> {
        if (name.isNullOrBlank() && ingredients.isNullOrEmpty() && category == null) {
            throw NotEnoughParametersException("No parameter supplied to multiple params search")
        }

        return if (!name.isNullOrBlank()) {
            // We got name -> the API call already returns all the details we need
            filterNameFirst(name, category, ingredients.orEmpty())
        } else if (category != null && ingredients.isNullOrEmpty()) {
            // We only have category and no ingredients -> we can directly return drinks only by category
            getDrinksByCategory(category)
        } else {
            // We have ingredients and maybe category -> we filter first by ingredients and than category
            val filteredByIngredients = filterByIngredients(ingredients)
            if (category != null) {
                filterByCategory(filteredByIngredients, category)
            } else {
                filteredByIngredients
            }
        }
    }

    override fun loadImage(imgView: ImageView, imageResourceId: String) {
        val imgUri = imageResourceId.toUri().buildUpon().scheme("https").build()
        imgView.load(imgUri) {
            error(R.drawable.ic_cocktail_default_icon)
            placeholder(R.drawable.ic_loading_img)
        }
    }

    // API call by name returns detail, that we can filter-out directly without another API call
    private suspend fun filterNameFirst(
        name: String,
        category: DrinkCategory?,
        ingredients: Array<out String>?
    ): List<DisplayableDrink> = getDrinksByName(name)
        .filter { it.satisfiesParams(category, ingredients.orEmpty()) }
        .map { DisplayableDrink(
            dataSourceTag = sourceTag,
            id = it.id,
            name = it.name,
            imageSource = it.imageSource
        )}

    // Calls all drinks with each ingredient and than intersect the results
    private suspend fun filterByIngredients(ingredients: Array<String>?): List<DisplayableDrink> =
        coroutineScope {
            val requests = mutableListOf<Deferred<List<DisplayableDrink>>>()
            ingredients!!.forEach { ingredient ->
                requests.add(async {
                    ensureActive()
                    getDrinksByIngredient(ingredient)
                })
            }
            requests.awaitAll().reduce { finalList, list ->
                finalList.intersect(list).toList()
            }
        }

    // Category is only in DisplayableDrinkDetail, so we call detail for each drink and than check category
    private suspend fun filterByCategory(
        drinks: List<DisplayableDrink>,
        category: DrinkCategory
    ): List<DisplayableDrink> = coroutineScope {
        val detailedRequests = mutableListOf<Deferred<DisplayableDrinkDetail?>>()
        drinks.forEach { drink ->
            detailedRequests.add(async {
                ensureActive()
                getDrinkDetail(drink.id)
            })
        }
        val filteredDetails = detailedRequests.awaitAll().filter { drinkDetail ->
            drinkDetail != null && drinkDetail.category == category
        }
        drinks.filter { drink ->
            filteredDetails.any { it!!.id == drink.id }
        }
    }

    companion object {
        fun testApi(): Boolean {
            // TODO Otestovat api - tady by šlo volat www.thecocktaildb.com/api/json/v1/1/list.php?a=list
            return true
        }
    }
}

// Does the DisplayableDrinkDetail satisfy given category and does it include given ingredients?
fun DisplayableDrinkDetail.satisfiesParams(
    category: DrinkCategory?,
    ingredients: Array<out String>
): Boolean {
    ingredients.forEach { ingredient ->
        if (!this.ingredients.containsKey(ingredient)) return false
    }
    return category == null || category == this.category
}
