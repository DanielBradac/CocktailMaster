package cz.bradacd.cocktailmaster.common

sealed class LoadingStatus {
    object Loading: LoadingStatus()
    object Success: LoadingStatus()
    data class Error(val errorMessageResource: Int): LoadingStatus()
}