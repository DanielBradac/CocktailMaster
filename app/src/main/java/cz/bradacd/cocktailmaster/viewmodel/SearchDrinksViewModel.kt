package cz.bradacd.cocktailmaster.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.bradacd.cocktailmaster.common.DrinkCategory
import cz.bradacd.cocktailmaster.datasource.browser.Browser
import cz.bradacd.cocktailmaster.datasource.browser.MultipleSourceDataCollector
import cz.bradacd.cocktailmaster.datasource.displayable.DisplayableDrink
import cz.bradacd.cocktailmaster.datasource.network.CocktailAPIBrowser
import kotlinx.coroutines.*
import java.lang.Exception
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.coroutines.coroutineContext
import kotlin.random.Random

class SearchDrinksViewModel: ViewModel() {

    private var _ingredientSuggestion = MutableLiveData<List<String>>()
    val ingredientSuggestion = _ingredientSuggestion

    private var _progressBarVisibility = MutableLiveData(View.GONE)
    val progressBarVisibility = _progressBarVisibility

    private var apiAvailable: Boolean = false
    private val dataCollector = MultipleSourceDataCollector()

    // Handle possible error inside retrofit - catch block wouldn't get it
    // TODO vyrobit si nějaký error handeling na tyhle případy
    private val coroutineExceptionHandler = CoroutineExceptionHandler{ _, throwable ->
        Log.e("CoroutineError", throwable.stackTraceToString())
    }

    init {
        viewModelScope.launch(coroutineExceptionHandler) {
            apiAvailable = CocktailAPIBrowser.isApiAvailable()
            if (apiAvailable) {
                dataCollector.addBrowser(CocktailAPIBrowser())
            }
            // TODO něco udělat když to nebude available
            //      - nějakej error bar a zablokovat online search
        }
    }

    fun updateSuggestionList(name: String) {
        _progressBarVisibility.value = View.VISIBLE
        viewModelScope.launch(coroutineExceptionHandler) {
            try {
                _ingredientSuggestion.value = dataCollector.collectIngredientsByName(name)
                    .sortedBy { name }
                    .map { it.name }
            } catch (e: Exception) {
                if (e is CancellationException) {
                    throw e
                }
                Log.e("CoroutineError", e.stackTraceToString())
            } finally {
                _progressBarVisibility.value = View.GONE
            }
        }
    }
}