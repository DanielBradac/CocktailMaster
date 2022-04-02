package cz.bradacd.cocktailmaster.datasource.browser

import cz.bradacd.cocktailmaster.common.DrinkCategory
import cz.bradacd.cocktailmaster.datasource.displayable.DisplayableDrink
import cz.bradacd.cocktailmaster.datasource.displayable.DisplayableDrinkDetail
import cz.bradacd.cocktailmaster.datasource.displayable.DisplayableIngredient

// Data have multiplesources - each source will implement this interace, which returns data in common displayable form
interface Browser {
    suspend fun getDrinksByName(name: String): List<DisplayableDrinkDetail>
    suspend fun getIngredientsByName(name: String): List<DisplayableIngredient>
    suspend fun getDrinksByCategory(cat: DrinkCategory): List<DisplayableDrink>
    suspend fun getDrinksMultipleParams(
        name: String? = null,
        category: DrinkCategory? = null,
        ingredients: List<String>? = null): List<DisplayableDrink>
}