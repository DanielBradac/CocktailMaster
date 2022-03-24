package cz.bradacd.cocktailmaster.datasource.database

import cz.bradacd.cocktailmaster.datasource.CocktailBrowser
import cz.bradacd.cocktailmaster.datasource.containers.DisplayableDrink

// Tohle bude vracet všechno v drinkcommon formě
class CocktailDBBrowser: CocktailBrowser {
    override suspend fun getDrinksByName(name: String): DisplayableDrink {
        TODO("Not yet implemented")
    }
}