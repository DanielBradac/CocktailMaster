package cz.bradacd.cocktailmaster.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.bradacd.cocktailmaster.datasource.network.CocktailApi
import cz.bradacd.cocktailmaster.datasource.network.mapping.Drink
import kotlinx.coroutines.*
import java.lang.Exception

class SearchDrinksViewModel: ViewModel() {

    private val logTag = "SearchDrinksViewModelLog"

    private var _drinks = MutableLiveData<List<Drink>>()
    val drinks = _drinks

    val status = MutableLiveData<String>()

    private val coroutineExceptionHandler = CoroutineExceptionHandler{ _, throwable ->
        Log.e(logTag, throwable.stackTraceToString())
        status.value = throwable.stackTraceToString()
    }

    fun getDrinksByName(name: String) {
        status.value = "loading"
        viewModelScope.launch(coroutineExceptionHandler) {
            try {
                // Network call - no need to switch Dispatchers, Retrofit will do that itself
                    // TODO zkusit si zalogovat kde je jaké vlákno
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
                // TODO otestovat co se stane, když tohle reálně vypadne - spadne to do coroutineExceptionHandler?
                if (e is CancellationException) {
                    throw e
                }
                status.value = "Error: ${e.message}"
            }
        }
    }
}