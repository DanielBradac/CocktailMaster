package cz.bradacd.cocktailmaster.datasource.displayable

import android.graphics.drawable.Drawable
import cz.bradacd.cocktailmaster.common.DrinkCategory

data class DisplayableDrinkDetail(
    val source: String,
    val id: String,
    val name: String,
    val category: DrinkCategory?,
    val videoSrc: String?,
    val instructions: String?,
    val imageSource: String?,
    // Key: ingredient name, value: ingredient measure
    val ingredients: Map<String, String>
)
