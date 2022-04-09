package cz.bradacd.cocktailmaster.common

enum class DrinkCategory(val apiName: String) {
    ALCOHOLIC(apiName = "Alcoholic"),
    NONALCOHOLIC(apiName = "Non alcoholic"),
    OPTALCOHOL(apiName = "Optional alcohol");

    override fun toString(): String = apiName
}

fun getCategoryByApiName(apiName: String): DrinkCategory? {
    return when(apiName) {
        DrinkCategory.ALCOHOLIC.apiName -> DrinkCategory.ALCOHOLIC
        DrinkCategory.NONALCOHOLIC.apiName -> DrinkCategory.NONALCOHOLIC
        DrinkCategory.OPTALCOHOL.apiName -> DrinkCategory.OPTALCOHOL
        else -> null
    }
}