package cz.bradacd.cocktailmaster.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.bradacd.cocktailmaster.datasource.network.CocktailApi
import cz.bradacd.cocktailmaster.datasource.network.mapping.Drink
import cz.bradacd.cocktailmaster.datasource.network.mapping.Drinks
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.math.log

class SearchDrinksViewModel: ViewModel() {

    private val logTag = "SearchDrinksViewModelLog"

    private var _drinks = MutableLiveData<List<Drink>>()
    val drinks = _drinks

    val status = MutableLiveData<String>()
    val apiAvailable = testApiAvailable()

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
                val fetchedDrinks = CocktailApi.fetchDrinksByName(name)

                _drinks.value = fetchedDrinks
                status.value = fetchedDrinks.size.toString()

                status.value = fetchedDrinks.let {
                    var message = ""
                    it.forEach { drink ->
                        message += "Drink: ${drink.idDrink}\n"
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

    // TODO
    fun testApiAvailable(): Boolean {
        return true
    }
}