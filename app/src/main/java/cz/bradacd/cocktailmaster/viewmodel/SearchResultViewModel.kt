package cz.bradacd.cocktailmaster.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.bradacd.cocktailmaster.common.LoadingStatus
import cz.bradacd.cocktailmaster.common.getCategoryByApiName
import cz.bradacd.cocktailmaster.common.getErrorRes
import cz.bradacd.cocktailmaster.datasource.browser.Browser
import cz.bradacd.cocktailmaster.datasource.browser.MultipleSourceDataCollector
import cz.bradacd.cocktailmaster.datasource.displayable.DisplayableDrink
import cz.bradacd.cocktailmaster.datasource.network.CocktailAPIBrowser
import cz.bradacd.cocktailmaster.ui.fragments.SearchResultFragmentArgs
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class SearchResultViewModel: ViewModel() {
    private lateinit var dataCollector: MultipleSourceDataCollector
    var drinks = listOf<DisplayableDrink>()
        private set

    private val _status = MutableLiveData<LoadingStatus>(LoadingStatus.Loading)
    val status: LiveData<LoadingStatus> = _status

    fun initDrinkListSearch(args: SearchResultFragmentArgs) {
        val browsers = mutableListOf<Browser>()

        // TODO DBBrowser not yet implemented
        //if (args.searchLocal) browsers.add(DBBrowser())
        if (args.searchOnline) browsers.add(CocktailAPIBrowser())
        dataCollector = MultipleSourceDataCollector(browsers)

        // This means the search was already done before (user navigated up), we don't need it
        if (status.value == LoadingStatus.Loading) {
            getSearchResult(args)
        }
    }

    // Handle possible error inside retrofit - catch block wouldn't get it
    // TODO vyrobit si nějaký error handeling na tyhle případy
    private val coroutineExceptionHandler = CoroutineExceptionHandler{ _, throwable ->
            _status.value = throwable.localizedMessage?.let { LoadingStatus.Error(getErrorRes(throwable)) }
    }

    private fun getSearchResult(args: SearchResultFragmentArgs) {
        viewModelScope.launch(coroutineExceptionHandler) {
            drinks = dataCollector.collectDrinksMultipleParams(
                name = args.drinkName,
                category = getCategoryByApiName(args.drinkCategory),
                ingredients = args.ingredients
            )
            _status.value = LoadingStatus.Success
        }
    }
}