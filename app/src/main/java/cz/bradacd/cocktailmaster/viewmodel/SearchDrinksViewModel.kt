package cz.bradacd.cocktailmaster.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.bradacd.cocktailmaster.common.DrinkCategory
import cz.bradacd.cocktailmaster.datasource.browser.MultipleSourceDataCollector
import cz.bradacd.cocktailmaster.datasource.displayable.DisplayableDrink
import cz.bradacd.cocktailmaster.datasource.network.CocktailAPIBrowser
import kotlinx.coroutines.*
import java.lang.Exception

class SearchDrinksViewModel: ViewModel() {

    private val logTag = "SearchDrinksViewModelLog"

    private var _drinks = MutableLiveData<List<DisplayableDrink>>()
    val drinks = _drinks

    private var _ingredientSuggestion = MutableLiveData<List<String>>()
    val ingredientSuggestion = _ingredientSuggestion

    private var _progressBarVisibility = MutableLiveData(View.GONE)
    val progressBarVisibility = _progressBarVisibility

    val apiAvailable = CocktailAPIBrowser.testApi()

    private val dataCollector: MultipleSourceDataCollector

    init {
        // TODO checknout jestli se připojíme k api a podle toho kdyžtak vynechat APIbrowser
        dataCollector = MultipleSourceDataCollector(listOf(CocktailAPIBrowser()))
    }

    // Handle possible error inside retrofit - catch block wouldn't get it
    private val coroutineExceptionHandler = CoroutineExceptionHandler{ _, throwable ->
        Log.e(logTag, throwable.stackTraceToString())
    }

    fun updateSuggestionList(name: String) {
        _progressBarVisibility.value = View.VISIBLE
        viewModelScope.launch(coroutineExceptionHandler) {
            try {
                _ingredientSuggestion.value = dataCollector.collectIngredientsByName(name)
                    .sortedBy { name }
                    .map { it.name }


                val test = dataCollector.collectDrinksMultipleParams(null, DrinkCategory.ALCOHOLIC, listOf("Aperol", "Prosecco"))
                Log.d(logTag, "Final result count: ${test.size}")
                Log.d(logTag, "${test}")
            } catch (e: Exception) {
                if (e is CancellationException) {
                    throw e
                }
                Log.e(logTag, e.stackTraceToString())
            } finally {
                _progressBarVisibility.value = View.GONE
            }
        }
    }


}