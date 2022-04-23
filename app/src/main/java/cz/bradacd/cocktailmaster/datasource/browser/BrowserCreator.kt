package cz.bradacd.cocktailmaster.datasource.browser

import cz.bradacd.cocktailmaster.common.BrowserUnavailableException
import cz.bradacd.cocktailmaster.datasource.database.DBBrowser
import cz.bradacd.cocktailmaster.datasource.network.CocktailAPIBrowser

fun createBrowserFromTag(sourceTag: String) = when (sourceTag) {
        DBBrowser().sourceTag -> DBBrowser()
        CocktailAPIBrowser().sourceTag -> CocktailAPIBrowser()
        else -> throw BrowserUnavailableException(
            "Source tag $sourceTag doesn't have any Browser implemented"
        )
    }