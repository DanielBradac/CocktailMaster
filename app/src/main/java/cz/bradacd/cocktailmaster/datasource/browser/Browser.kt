package cz.bradacd.cocktailmaster.datasource.browser

import android.widget.ImageView
import cz.bradacd.cocktailmaster.common.DrinkCategory
import cz.bradacd.cocktailmaster.datasource.displayable.DisplayableDrink
import cz.bradacd.cocktailmaster.datasource.displayable.DisplayableDrinkDetail
import cz.bradacd.cocktailmaster.datasource.displayable.DisplayableIngredient

// Data have multiple sources - each source will implement this interface, which returns data in common displayable form
interface Browser {
    val sourceTag: String
    suspend fun getDrinkDetail(id: String): DisplayableDrinkDetail?
    suspend fun getIngredientsByName(name: String): List<DisplayableIngredient>
    suspend fun getDrinksMultipleParams(
        name: String? = null,
        category: DrinkCategory? = null,
        ingredients: Array<String>? = null,
        loadImages: Boolean = true): List<DisplayableDrink>
    fun loadImage(imgView: ImageView, imageResourceId: String)
}

