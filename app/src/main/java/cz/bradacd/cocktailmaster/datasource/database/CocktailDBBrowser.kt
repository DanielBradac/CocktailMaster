package cz.bradacd.cocktailmaster.datasource.database

import cz.bradacd.cocktailmaster.datasource.browser.CocktailBrowser
import cz.bradacd.cocktailmaster.datasource.containers.DisplayableDrink

// Tohle bude vracet všechno v drinkcommon formě
class CocktailDBBrowser: CocktailBrowser {
    override suspend fun getDrinksByName(name: String): List<DisplayableDrink> {
        TODO("Not yet implemented")
    }
}