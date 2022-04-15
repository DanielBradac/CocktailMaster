package cz.bradacd.cocktailmaster.ui

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import cz.bradacd.cocktailmaster.datasource.browser.MultipleSourceDataCollector
import cz.bradacd.cocktailmaster.datasource.displayable.DisplayableDrink
import cz.bradacd.cocktailmaster.datasource.network.CocktailAPIBrowser


// TODO na zobrazení obrázku je potřeba context, takže to udělat podobně jako je v Mars photos
// TODO - nějak vymyslet jak šikovně říct odkud se bude vlastně ten obrázek brát
@BindingAdapter("drink", "dataCollector")
fun bindImage(imgView: ImageView, drink: DisplayableDrink, dataCollector: MultipleSourceDataCollector) {
    val browser = CocktailAPIBrowser()
    browser.loadImage(imgView, drink.imageSource!!)

    dataCollector.loadImage(imgView, drink.dataSourceTag, drink.imageSource)
}
