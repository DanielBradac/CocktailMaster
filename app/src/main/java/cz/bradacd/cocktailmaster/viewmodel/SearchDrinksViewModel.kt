package cz.bradacd.cocktailmaster.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.bradacd.cocktailmaster.datasource.browser.DataCollector
import cz.bradacd.cocktailmaster.datasource.containers.DisplayableDrink
import cz.bradacd.cocktailmaster.datasource.network.CocktailAPIBrowser
import kotlinx.coroutines.*
import java.lang.Exception

class SearchDrinksViewModel: ViewModel() {

    private val logTag = "SearchDrinksViewModelLog"

    private var _drinks = MutableLiveData<List<DisplayableDrink>>()
    val drinks = _drinks

    val status = MutableLiveData<String>()
    val apiAvailable = CocktailAPIBrowser.testApi()

    private val dataCollector: DataCollector

    init {
        // TODO checknout jestli se připojíme k api a podle toho kdyžtak vynechat APIbrowser
        dataCollector = DataCollector(listOf(CocktailAPIBrowser()))
    }

    // Handle possible error inside retrofit - catch block wouldn't get it
    private val coroutineExceptionHandler = CoroutineExceptionHandler{ _, throwable ->
        Log.e(logTag, throwable.stackTraceToString())
        status.value = throwable.stackTraceToString()
    }

    fun getDrinksByName(name: String) {
        status.value = "loading"
        viewModelScope.launch(coroutineExceptionHandler) {
            try {
                // Network call - no need to switch Dispatchers, Retrofit will do that itself
                val fetchedDrinks = dataCollector.collectDrinksByName(name)

                _drinks.value = fetchedDrinks.sortedBy { it.name }
                status.value = fetchedDrinks.size.toString()

                status.value = _drinks.value.let {
                    var message = ""
                    it!!.forEach { drink ->
                        message += "Drink: ${drink.name}\n"
                    }
                    message
                }

            } catch (e: Exception) {
                if (e is CancellationException) {
                    throw e
                }
                status.value = "Error: ${e.message}"
            }
        }
    }
}