package cz.bradacd.cocktailmaster.datasource.browser

import cz.bradacd.cocktailmaster.datasource.containers.DisplayableIngredient

// Cocktail ingredient has multiple possible sources, each has its own browser implementation
interface IngredientBrowser {
    suspend fun getIngredientsByName(name: String): List<DisplayableIngredient>
}