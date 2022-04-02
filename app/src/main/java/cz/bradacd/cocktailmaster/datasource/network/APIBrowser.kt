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
class CocktailAPIBrowser: Browser {
    override suspend fun getDrinksByName(name: String): List<DisplayableDrinkDetail> {
        return CocktailApi.fetchDrinksByName(name).map {
            it.toDisplayable()
        }
    }

    override suspend fun getIngredientsByName(name: String): List<DisplayableIngredient> {
        return CocktailApi.fetchIngredientsByName(name).map {
            it.toDisplayable()
        }
    }

    override suspend fun getDrinksByCategory(cat: DrinkCategory): List<DisplayableDrink> {
        return CocktailApi.fetchIngredientsByCategory(cat).map {
            it.toDisplayable()
        }
    }

    private suspend fun getDrinksByIngredient(ingredientName: String): List<DisplayableDrink> {
        delay(2000)
        return CocktailApi.fetchDrinksByIngredient(ingredientName).map {
            it.toDisplayable()
        }
    }

    override suspend fun getDrinksMultipleParams(
        name: String?,
        category: DrinkCategory?,
        ingredients: List<String>?
    ): List<DisplayableDrink> {
        Log.d("SearchDrinksViewModelLog", "Calling with $name, $category, $ingredients")
        if (name.isNullOrBlank() && ingredients.isNullOrEmpty() && category == null) {
            throw NotEnoughParametersException("No parameter supplied to multiple params search")
        }
        // We got name - the API call already returns all the details we need
        if (!name.isNullOrBlank()) {
            val drinksByName = getDrinksByName(name)
            return drinksByName
                .filter { it.satisfiesParams(category, ingredients.orEmpty()) }
                .map { DisplayableDrink(it.id, it.name, it.thumbImgSrc) }
        }
        // We only have category and no ingredients
        if (category != null && ingredients.isNullOrEmpty()) {
            return getDrinksByCategory(category)
        }

        // We have ingredients, we must fetch data for each category in parallel
        // TODO funkčně asi ok, ale je potřbe amrknout na thready a zkusit delay, atd.
        // TODO odfiltrovat výsledek podle kategorie - tady je potřeba vyhledat detaily, zase paralalně, a kouknout jestli jsou s kategorií ok
        return coroutineScope {
            val requests = mutableListOf<Deferred<List<DisplayableDrink>>>()
            ingredients!!.forEach { ingredient ->
                requests.add(async { getDrinksByIngredient(ingredient) })
            }
            // We take intersection of all the results
            requests.awaitAll().reduce {
                finalList, list ->
                finalList.intersect(list).toList()
                }
        }
    }

    companion object {
        fun testApi(): Boolean {
            // Otestovat api - nějak
            return true
        }
    }
}

// Does the DisplayableDrinkDetail satisfy given category and does it include given ingredients?
fun DisplayableDrinkDetail.satisfiesParams(category: DrinkCategory?, ingredients: List<String>): Boolean {
    ingredients.forEach { ingredient ->
        if (!this.ingredients.containsKey(ingredient)) return false
    }
    return category == null || category == this.category
}
