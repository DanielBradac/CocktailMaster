package cz.bradacd.cocktailmaster.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.bradacd.cocktailmaster.datasource.browser.MultipleSourceDataCollector
import cz.bradacd.cocktailmaster.datasource.network.CocktailAPIBrowser
import kotlinx.coroutines.*
import java.lang.Exception

class SearchDrinksViewModel: ViewModel() {
    private val dataCollector = MultipleSourceDataCollector()

    private var _ingredientSuggestion = MutableLiveData<List<String>>()
    val ingredientSuggestion = _ingredientSuggestion

    private var _progressBarVisibility = MutableLiveData(View.GONE)
    val progressBarVisibility = _progressBarVisibility

    private var _apiAvailable = MutableLiveData<Boolean>()
    val apiAvailable = _apiAvailable

    // Handle possible error inside retrofit - catch block wouldn't get it
    // TODO vyrobit si nějaký error handeling na tyhle případy
    private val coroutineExceptionHandler = CoroutineExceptionHandler{ _, throwable ->
        Log.e("CoroutineError", throwable.stackTraceToString())
    }

    init {
        viewModelScope.launch(coroutineExceptionHandler) {
            _apiAvailable.value = CocktailAPIBrowser.isApiAvailable()
            if (_apiAvailable.value!!) {
                dataCollector.addBrowser(CocktailAPIBrowser())
            }
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
                if (e is CancellationException) throw e
                Log.e("CoroutineError", e.stackTraceToString())
            } finally {
                _progressBarVisibility.value = View.GONE
            }
        }
    }
}