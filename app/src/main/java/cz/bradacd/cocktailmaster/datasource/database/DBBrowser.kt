package cz.bradacd.cocktailmaster.datasource.database

import cz.bradacd.cocktailmaster.common.DrinkCategory
import cz.bradacd.cocktailmaster.datasource.browser.Browser
import cz.bradacd.cocktailmaster.datasource.displayable.DisplayableDrink
import cz.bradacd.cocktailmaster.datasource.displayable.DisplayableDrinkDetail
import cz.bradacd.cocktailmaster.datasource.displayable.DisplayableIngredient

// Tohle bude vracet všechno v drinkcommon formě
class DBBrowser: Browser {
    override suspend fun getDrinksByName(name: String): List<DisplayableDrinkDetail> {
        TODO("Not yet implemented")
    }

    override suspend fun getIngredientsByName(name: String): List<DisplayableIngredient> {
        TODO("Not yet implemented")
    }

    override suspend fun getDrinksByCategory(cat: DrinkCategory): List<DisplayableDrink> {
        TODO("Not yet implemented")
    }

    override suspend fun getDrinksMultipleParams(
        name: String?,
        category: DrinkCategory?,
        ingredients: List<String>?
    ): List<DisplayableDrink> {
        TODO("Not yet implemented")
    }
}