package cz.bradacd.cocktailmaster.datasource.browser

import cz.bradacd.cocktailmaster.datasource.containers.DisplayableDrink

// Cocktail has multiple possible sources, each has its own browser implementation
interface IngredientBrowser {
    suspend fun getIngredientsByName(name: String): DisplayableDrink
}