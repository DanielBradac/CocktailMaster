package cz.bradacd.cocktailmaster.datasource.network.mapping

import com.squareup.moshi.Json

data class Ingredients(
    @Json(name = "ingredients") val ingredient: List<Ingredient>
)

data class Ingredient (
    @Json(name = "idIngredient") val idIngredient: String,
    @Json(name = "strIngredient") val strIngredient: String,
)
