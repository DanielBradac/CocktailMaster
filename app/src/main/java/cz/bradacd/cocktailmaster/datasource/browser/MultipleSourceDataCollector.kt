package cz.bradacd.cocktailmaster.datasource.browser

import android.widget.ImageView
import cz.bradacd.cocktailmaster.R
import cz.bradacd.cocktailmaster.common.DrinkCategory
import cz.bradacd.cocktailmaster.datasource.displayable.DisplayableDrink
import cz.bradacd.cocktailmaster.datasource.displayable.DisplayableIngredient
import kotlinx.coroutines.*

// This class collects data from all the browsers it gets in constructor and merge them together
// TODO tady se bude opakovat kód, chtělo by to nějak zobecnit
// TODO otestovat jak funguje ensureActive
class MultipleSourceDataCollector() {
    private val logTag = "DataCollector"
    private val browsers = mutableListOf<Browser>()

    fun addBrowser(browser: Browser) = browsers.add(browser)

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

    fun loadImage(imgView: ImageView, dataSourceTag: String?, imageSource: String?) {
        if (dataSourceTag.isNullOrBlank() || imageSource.isNullOrBlank()) return

        browsers.find { it.sourceTag == dataSourceTag }.let { browser ->
            if (browser != null) {
                browser.loadImage(imgView, imageSource)
                return
            }
            // No suitable browsers found - set default icon
            imgView.setImageResource(R.drawable.ic_cocktail_default_icon)
        }
    }
}
