package cz.bradacd.cocktailmaster.datasource.network.mapping

import com.squareup.moshi.Json

data class Drinks(
    @Json(name = "drinks") val drinks: List<Drink>
)

data class Drink(
    @Json(name = "idDrink") val idDrink: String,
    @Json(name = "strDrink") val strDrink: String,
)