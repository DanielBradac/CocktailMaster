package cz.bradacd.cocktailmaster.datasource.network.mapping

import com.squareup.moshi.Json

data class APITest(
    @Json(name = "drinks") val entities: List<APITestEntity>?
)

data class APITestEntity(
    @Json(name = "strAlcoholic") val alcoholic: String
)