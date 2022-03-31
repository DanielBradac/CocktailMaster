package cz.bradacd.cocktailmaster.datasource.network

import android.util.Log
import cz.bradacd.cocktailmaster.datasource.browser.CocktailBrowser
import cz.bradacd.cocktailmaster.datasource.browser.IngredientBrowser
import cz.bradacd.cocktailmaster.datasource.containers.DisplayableDrink
import cz.bradacd.cocktailmaster.datasource.containers.DisplayableIngredient
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay

val logTag = "CocktailAPIBrowserLog"

// Tohle bude vracet všechno v drinkcommon formě
class CocktailAPIBrowser: CocktailBrowser, IngredientBrowser {


    override suspend fun getDrinksByName(name: String): List<DisplayableDrink> {
        delay(1000)
        Log.d(logTag, "I am fast and working from ctx: ${currentCoroutineContext()} and thread: ${Thread.currentThread().name}")
        return CocktailApi.fetchDrinksByName(name).map {
            DisplayableDrink(
                it.idDrink,
                it.strDrink
            )
        }
    }

    override suspend fun getIngredientsByName(name: String): List<DisplayableIngredient> {
        return CocktailApi.fetchIngredientsByName(name).map {
            DisplayableIngredient(
                it.idIngredient,
                it.strIngredient
            )
        }
    }

    companion object {
        fun testApi(): Boolean {
            // Otestovat api - nějak
            return true
        }
    }
}