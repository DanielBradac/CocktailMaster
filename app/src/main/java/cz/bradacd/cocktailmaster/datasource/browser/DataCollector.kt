package cz.bradacd.cocktailmaster.datasource.browser

import cz.bradacd.cocktailmaster.datasource.containers.DisplayableDrink
import kotlinx.coroutines.*

// This class collects data from sources it gets in constructor
class DataCollector(private val browsers: List<CocktailBrowser>) {
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
}
