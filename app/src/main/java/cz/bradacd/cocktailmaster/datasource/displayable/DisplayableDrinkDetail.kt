package cz.bradacd.cocktailmaster.datasource.displayable

import cz.bradacd.cocktailmaster.common.DrinkCategory

data class DisplayableDrinkDetail(
    val id: String,
    val name: String,
    val category: DrinkCategory?,
    val videoSrc: String?,
    val instructions: String?,
    val thumbImgSrc: String?,
    // Key: ingredient name, value: ingredient measure
    val ingredients: Map<String, String>
)
