package cz.bradacd.cocktailmaster.datasource.browser

import cz.bradacd.cocktailmaster.common.DrinkCategory
import cz.bradacd.cocktailmaster.datasource.displayable.DisplayableDrink
import cz.bradacd.cocktailmaster.datasource.displayable.DisplayableIngredient
import kotlinx.coroutines.*

// This class collects data from all the browsers it gets in constructor and merge them together
// TODO tady se bude opakovat kód, chtělo by to nějak zobecnit
// TODO otestovat jak funguje ensureActive
class MultipleSourceDataCollector(private val browsers: List<Browser>) {
    private val logTag = "DataCollector"

    suspend fun collectIngredientsByName(name: String): List<DisplayableIngredient> {
        return coroutineScope {
            val requests = mutableListOf<Deferred<List<DisplayableIngredient>>>()
            browsers.forEach { browser ->
                requests.add(async {
                    ensureActive()
                    browser.getIngredientsByName(name)
                })
            }
            return@coroutineScope requests.awaitAll().flatten()
        }
    }

    suspend fun collectDrinksMultipleParams(name: String?, category: DrinkCategory?, ingredients: Array<String>?): List<DisplayableDrink> {
        return coroutineScope {
            val requests = mutableListOf<Deferred<List<DisplayableDrink>>>()
            browsers.forEach { browser ->
                requests.add(async {
                    ensureActive()
                    browser.getDrinksMultipleParams(name, category, ingredients)
                })
            }
            return@coroutineScope requests.awaitAll().flatten()
        }
    }
}
