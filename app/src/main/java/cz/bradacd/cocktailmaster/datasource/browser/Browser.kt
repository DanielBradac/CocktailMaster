package cz.bradacd.cocktailmaster.datasource.browser

import android.graphics.drawable.Drawable
import cz.bradacd.cocktailmaster.common.DrinkCategory
import cz.bradacd.cocktailmaster.datasource.displayable.DisplayableDrink
import cz.bradacd.cocktailmaster.datasource.displayable.DisplayableDrinkDetail
import cz.bradacd.cocktailmaster.datasource.displayable.DisplayableIngredient

// Data have multiplesources - each source will implement this interace, which returns data in common displayable form
interface Browser {
    val sourceTag: String
    suspend fun getDrinkDetail(id: String, loadImage: Boolean = true): DisplayableDrinkDetail?
    suspend fun getIngredientsByName(name: String): List<DisplayableIngredient>
    suspend fun getDrinksMultipleParams(
        name: String? = null,
        category: DrinkCategory? = null,
        ingredients: Array<String>? = null,
        loadImages: Boolean = true): List<DisplayableDrink>
}