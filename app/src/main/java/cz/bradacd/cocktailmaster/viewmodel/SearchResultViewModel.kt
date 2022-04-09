package cz.bradacd.cocktailmaster.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.bradacd.cocktailmaster.common.getCategoryByApiName
import cz.bradacd.cocktailmaster.datasource.browser.Browser
import cz.bradacd.cocktailmaster.datasource.browser.MultipleSourceDataCollector
import cz.bradacd.cocktailmaster.datasource.displayable.DisplayableDrink
import cz.bradacd.cocktailmaster.datasource.network.CocktailAPIBrowser
import cz.bradacd.cocktailmaster.ui.fragments.SearchResultFragmentArgs
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class SearchResultViewModel: ViewModel() {
    private lateinit var dataCollector: MultipleSourceDataCollector

    private val _drinks = MutableLiveData<List<DisplayableDrink>>()
    val drinks: LiveData<List<DisplayableDrink>> = _drinks

    fun initResultSearch(args: SearchResultFragmentArgs) {
        val browsers = mutableListOf<Browser>()

        // TODO DBBrowser not yet implemented
        //if (args.searchLocal) browsers.add(DBBrowser())
        if (args.searchOnline) browsers.add(CocktailAPIBrowser())
        dataCollector = MultipleSourceDataCollector(browsers)
        getSearchResult(args)
    }

    // Handle possible error inside retrofit - catch block wouldn't get it
    // TODO vyrobit si nějaký error handeling na tyhle případy
    private val coroutineExceptionHandler = CoroutineExceptionHandler{ _, throwable ->
        Log.e("RetrofitBroken", throwable.stackTraceToString())
    }

    private fun getSearchResult(args: SearchResultFragmentArgs) {
        viewModelScope.launch(coroutineExceptionHandler) {
            _drinks.value = dataCollector.collectDrinksMultipleParams(
                name = args.drinkName,
                category = getCategoryByApiName(args.drinkCategory),
                ingredients = args.ingredients
            )
        }
    }
}