package cz.bradacd.cocktailmaster.common

import cz.bradacd.cocktailmaster.R
import java.net.UnknownHostException

// Returns string resource which should be displayed as error message
fun getErrorRes(t: Throwable) = when(t) {
    is UnknownHostException -> R.string.error_internet_connection
    else -> R.string.error_unknown
}