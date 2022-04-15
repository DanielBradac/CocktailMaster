package cz.bradacd.cocktailmaster.datasource.displayable

import cz.bradacd.cocktailmaster.common.DrinkCategory

data class DisplayableDrinkDetail(
    val dataSourceTag: String,
    val id: String,
    val name: String,
    val category: DrinkCategory?,
    val videoSrc: String?,
    val instructions: String?,
    val imageSource: String?,
    // Key: ingredient name, value: ingredient measure
    val ingredients: Map<String, String>
)
