package cz.bradacd.cocktailmaster.datasource.browser

import cz.bradacd.cocktailmaster.datasource.containers.DisplayableDrink
import cz.bradacd.cocktailmaster.datasource.containers.DisplayableIngredient

// Cocktail has multiple possible sources, each has its own browser implementation
interface CocktailBrowser {
    suspend fun getDrinksByName(name: String): List<DisplayableDrink>
    suspend fun getIngredientsByName(name: String): List<DisplayableIngredient>
}