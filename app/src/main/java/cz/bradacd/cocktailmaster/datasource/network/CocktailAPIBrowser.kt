package cz.bradacd.cocktailmaster.datasource.network

import cz.bradacd.cocktailmaster.datasource.browser.CocktailBrowser
import cz.bradacd.cocktailmaster.datasource.containers.DisplayableDrink

// Tohle bude vracet všechno v drinkcommon formě
class CocktailAPIBrowser: CocktailBrowser {
    override suspend fun getDrinksByName(name: String): DisplayableDrink {
        TODO("Not yet implemented")
    }
}