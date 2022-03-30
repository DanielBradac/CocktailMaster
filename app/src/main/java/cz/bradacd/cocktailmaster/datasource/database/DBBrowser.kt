package cz.bradacd.cocktailmaster.datasource.database

import cz.bradacd.cocktailmaster.datasource.browser.CocktailBrowser
import cz.bradacd.cocktailmaster.datasource.containers.DisplayableDrink
import cz.bradacd.cocktailmaster.datasource.containers.DisplayableIngredient

// Tohle bude vracet všechno v drinkcommon formě
class DBBrowser: CocktailBrowser {
    override suspend fun getDrinksByName(name: String): List<DisplayableDrink> {
        TODO("Not yet implemented")
    }

    override suspend fun getIngredientsByName(name: String): List<DisplayableIngredient> {
        TODO("Not yet implemented")
    }
}