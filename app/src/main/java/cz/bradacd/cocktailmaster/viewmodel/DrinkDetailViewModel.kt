package cz.bradacd.cocktailmaster.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.bradacd.cocktailmaster.common.LoadingStatus
import cz.bradacd.cocktailmaster.datasource.browser.Browser
import cz.bradacd.cocktailmaster.datasource.browser.createBrowserFromTag
import cz.bradacd.cocktailmaster.datasource.displayable.DisplayableDrinkDetail
import cz.bradacd.cocktailmaster.ui.fragments.DrinkDetailFragmentArgs
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception

class DrinkDetailViewModel: ViewModel() {

    private val _status = MutableLiveData<LoadingStatus>(LoadingStatus.Loading)
    val status: LiveData<LoadingStatus> = _status

    private lateinit var browser: Browser

    private val _drinkDetail = MutableLiveData<DisplayableDrinkDetail>()
    val drinkDetail: LiveData<DisplayableDrinkDetail> = _drinkDetail


    // Handle possible error inside retrofit - catch block wouldn't get it
    // TODO vyrobit si nějaký error handeling na tyhle případy
    private val coroutineExceptionHandler = CoroutineExceptionHandler{ _, throwable ->
        _status.value = throwable.localizedMessage?.let { LoadingStatus.Error(it) }
    }

    fun initDrinkDetailSearch(args: DrinkDetailFragmentArgs) {
        try {
            browser = createBrowserFromTag(args.dataSourceTag)
            viewModelScope.launch(coroutineExceptionHandler) {
                _drinkDetail.value = browser.getDrinkDetail(args.drinkId)
                if (drinkDetail.value == null) {
                    _status.value = LoadingStatus.Error("Unable to find drink detail")
                } else {
                    _status.value = LoadingStatus.Success
                }
            }
        } catch (e: Exception) {
            _status.value = e.localizedMessage?.let { LoadingStatus.Error(it) }
        }
    }
}