package cz.bradacd.cocktailmaster.datasource.network

import android.util.Log
import cz.bradacd.cocktailmaster.common.DrinkCategory
import cz.bradacd.cocktailmaster.common.NotEnoughParametersException
import cz.bradacd.cocktailmaster.datasource.browser.Browser
import cz.bradacd.cocktailmaster.datasource.displayable.DisplayableDrink
import cz.bradacd.cocktailmaster.datasource.displayable.DisplayableDrinkDetail
import cz.bradacd.cocktailmaster.datasource.displayable.DisplayableIngredient
import cz.bradacd.cocktailmaster.datasource.network.mapping.toDisplayable
import kotlinx.coroutines.*

const val logTag = "CocktailAPIBrowserLog"

// Tohle bude vracet všechno v displayable formě
// TODO na vhodný místa dát ensureActive()
class CocktailAPIBrowser : Browser {
    override suspend fun getDrinksByName(name: String): List<DisplayableDrinkDetail> =
        CocktailApi.fetchDrinksByName(name).map { it.toDisplayable() }

    override suspend fun getIngredientsByName(name: String): List<DisplayableIngredient> =
        CocktailApi.fetchIngredientsByName(name).map { it.toDisplayable() }

    override suspend fun getDrinksByCategory(cat: DrinkCategory): List<DisplayableDrink> =
        CocktailApi.fetchIngredientsByCategory(cat).map { it.toDisplayable() }

    private suspend fun getDrinksByIngredient(ingredientName: String): List<DisplayableDrink> =
        CocktailApi.fetchDrinksByIngredient(ingredientName).map { it.toDisplayable() }

    override suspend fun getDrinkDetail(id: String): DisplayableDrinkDetail? =
        CocktailApi.fetchDrinkDetailById(id)?.toDisplayable()

    // TODO otestovat si coroutiny ve stylu výpis vláken, delay, atd..
    override suspend fun getDrinksMultipleParams(
        name: String?,
        category: DrinkCategory?,
        ingredients: List<String>?
    ): List<DisplayableDrink> {
        Log.d("SearchDrinksViewModelLog", "Calling with $name, $category, $ingredients")
        if (name.isNullOrBlank() && ingredients.isNullOrEmpty() && category == null) {
            throw NotEnoughParametersException("No parameter supplied to multiple params search")
        }
        // We got name -> the API call already returns all the details we need
        if (!name.isNullOrBlank()) {
            return filterNameFirst(name, category, ingredients.orEmpty())
        }
        // We only have category and no ingredients -> we can directly return drinks only by category
        if (category != null && ingredients.isNullOrEmpty()) {
            return getDrinksByCategory(category)
        }
        // We have ingredients and maybe category -> we filter first by ingredients and than category
        val filteredByIngredients = filterByIngredients(ingredients)
        if (category != null) {
            return filterByCategory(filteredByIngredients, category)
        }
        return filteredByIngredients
    }

    // API call by name returns detail, that we can filter-out directly without another API call
    private suspend fun filterNameFirst(
        name: String,
        category: DrinkCategory?,
        ingredients: List<String>?
    ): List<DisplayableDrink> = getDrinksByName(name)
        .filter { it.satisfiesParams(category, ingredients.orEmpty()) }
        .map { DisplayableDrink(it.id, it.name, it.thumbImgSrc) }

    // Calls all drinks with each ingredient and than intersect the results
    private suspend fun filterByIngredients(ingredients: List<String>?): List<DisplayableDrink> =
        coroutineScope {
            val requests = mutableListOf<Deferred<List<DisplayableDrink>>>()
            ingredients!!.forEach { ingredient ->
                requests.add(async { getDrinksByIngredient(ingredient) })
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
            detailedRequests.add(async { getDrinkDetail(drink.id) })
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
            // TODO Otestovat api - nějak
            return true
        }
    }
}

// Does the DisplayableDrinkDetail satisfy given category and does it include given ingredients?
fun DisplayableDrinkDetail.satisfiesParams(
    category: DrinkCategory?,
    ingredients: List<String>
): Boolean {
    ingredients.forEach { ingredient ->
        if (!this.ingredients.containsKey(ingredient)) return false
    }
    return category == null || category == this.category
}
