package cz.bradacd.cocktailmaster.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.bradacd.cocktailmaster.datasource.browser.MultipleSourceDataCollector
import cz.bradacd.cocktailmaster.datasource.containers.DisplayableDrink
import cz.bradacd.cocktailmaster.datasource.containers.DisplayableIngredient
import cz.bradacd.cocktailmaster.datasource.network.CocktailAPIBrowser
import kotlinx.coroutines.*
import java.lang.Exception

class SearchDrinksViewModel: ViewModel() {

    private val logTag = "SearchDrinksViewModelLog"

    private var _drinks = MutableLiveData<List<DisplayableDrink>>()
    val drinks = _drinks

    private var _ingredientSuggestion = MutableLiveData<List<DisplayableIngredient>>()
    val ingredientSuggestion = _ingredientSuggestion

    val status = MutableLiveData<String>()
    val apiAvailable = CocktailAPIBrowser.testApi()

    private val dataCollector: MultipleSourceDataCollector

    init {
        // TODO checknout jestli se připojíme k api a podle toho kdyžtak vynechat APIbrowser
        dataCollector = MultipleSourceDataCollector(listOf(CocktailAPIBrowser()))
    }

    // Handle possible error inside retrofit - catch block wouldn't get it
    private val coroutineExceptionHandler = CoroutineExceptionHandler{ _, throwable ->
        Log.e(logTag, throwable.stackTraceToString())
        status.value = throwable.stackTraceToString()
    }

    fun getIngredientsByName(name: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
            try {
                val fetchedIngredients = dataCollector.collectIngredientsByName(name)
                _ingredientSuggestion.value = fetchedIngredients.sortedBy { name }
            } catch (e: Exception) {
                if (e is CancellationException) {
                    throw e
                }
                status.value = "Error: ${e.message}"
            }
        }
    }
}