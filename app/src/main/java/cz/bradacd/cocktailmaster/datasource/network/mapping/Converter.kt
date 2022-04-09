package cz.bradacd.cocktailmaster.datasource.network.mapping

import android.util.Log
import cz.bradacd.cocktailmaster.common.DrinkCategory
import cz.bradacd.cocktailmaster.datasource.displayable.DisplayableDrink
import cz.bradacd.cocktailmaster.datasource.displayable.DisplayableDrinkDetail
import cz.bradacd.cocktailmaster.datasource.displayable.DisplayableIngredient

// Functions that convert to displayable form

fun DetailedDrink.toDisplayable(source: String): DisplayableDrinkDetail {
    val ingMap = HashMap<String, String>()
    if (!strIngredient1.isNullOrBlank() && !strMeasure1.isNullOrBlank()) ingMap[strIngredient1] = strMeasure1
    if (!strIngredient2.isNullOrBlank() && !strMeasure2.isNullOrBlank()) ingMap[strIngredient2] = strMeasure2
    if (!strIngredient3.isNullOrBlank() && !strMeasure3.isNullOrBlank()) ingMap[strIngredient3] = strMeasure3
    if (!strIngredient4.isNullOrBlank() && !strMeasure4.isNullOrBlank()) ingMap[strIngredient4] = strMeasure4
    if (!strIngredient5.isNullOrBlank() && !strMeasure5.isNullOrBlank()) ingMap[strIngredient5] = strMeasure5
    if (!strIngredient6.isNullOrBlank() && !strMeasure6.isNullOrBlank()) ingMap[strIngredient6] = strMeasure6
    if (!strIngredient7.isNullOrBlank() && !strMeasure7.isNullOrBlank()) ingMap[strIngredient7] = strMeasure7
    if (!strIngredient8.isNullOrBlank() && !strMeasure8.isNullOrBlank()) ingMap[strIngredient8] = strMeasure8
    if (!strIngredient9.isNullOrBlank() && !strMeasure9.isNullOrBlank()) ingMap[strIngredient9] = strMeasure9
    if (!strIngredient10.isNullOrBlank() && !strMeasure10.isNullOrBlank()) ingMap[strIngredient10] = strMeasure10
    if (!strIngredient11.isNullOrBlank() && !strMeasure11.isNullOrBlank()) ingMap[strIngredient11] = strMeasure11
    if (!strIngredient12.isNullOrBlank() && !strMeasure12.isNullOrBlank()) ingMap[strIngredient12] = strMeasure12
    if (!strIngredient13.isNullOrBlank() && !strMeasure13.isNullOrBlank()) ingMap[strIngredient13] = strMeasure13
    if (!strIngredient14.isNullOrBlank() && !strMeasure14.isNullOrBlank()) ingMap[strIngredient14] = strMeasure14
    if (!strIngredient15.isNullOrBlank() && !strMeasure15.isNullOrBlank()) ingMap[strIngredient15] = strMeasure15

    val category = when(strAlcoholic) {
        DrinkCategory.ALCOHOLIC.apiName -> DrinkCategory.ALCOHOLIC
        DrinkCategory.NONALCOHOLIC.apiName -> DrinkCategory.NONALCOHOLIC
        DrinkCategory.OPTALCOHOL.apiName -> DrinkCategory.OPTALCOHOL
        else -> null
    }

    return DisplayableDrinkDetail(
        id = idDrink,
        name = strDrink,
        category = category,
        videoSrc = strVideo,
        instructions = strInstructions,
        thumbImgSrc = strDrinkThumb,
        ingredients = ingMap,
        source = source
    )
}

fun Drink.toDisplayable(source: String): DisplayableDrink = DisplayableDrink(
        id = idDrink,
        name = strDrink,
        thumbImgSrc = strDrinkThumb,
        source = source
    )

fun Ingredient.toDisplayable(source: String): DisplayableIngredient = DisplayableIngredient(
    id = idIngredient,
    name = strIngredient,
    source = source
)

