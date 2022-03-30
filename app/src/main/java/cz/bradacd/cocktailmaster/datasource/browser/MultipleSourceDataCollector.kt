package cz.bradacd.cocktailmaster.datasource.browser

import cz.bradacd.cocktailmaster.datasource.containers.DisplayableDrink
import cz.bradacd.cocktailmaster.datasource.containers.DisplayableIngredient
import kotlinx.coroutines.*

// This class collects data from all the sources it gets in constructor
class MultipleSourceDataCollector(private val browsers: List<CocktailBrowser>) {
    private val logTag = "DataCollector"


    suspend fun collectDrinksByName(name: String): List<DisplayableDrink> {
        return coroutineScope {
            val requests = mutableListOf<Deferred<List<DisplayableDrink>>>()
            browsers.forEach { browser ->
                requests.add(async { browser.getDrinksByName(name) })
            }
            return@coroutineScope requests.awaitAll().flatten()
        }
    }

    suspend fun collectIngredientsByName(name: String): List<DisplayableIngredient> {
        return coroutineScope {
            val requests = mutableListOf<Deferred<List<DisplayableIngredient>>>()
            browsers.forEach { browser ->
                requests.add(async { browser.getIngredientsByName(name) })
            }
            return@coroutineScope requests.awaitAll().flatten()
        }
    }
}
