package cz.bradacd.cocktailmaster.common

enum class DrinkCategory(val apiName: String) {
    ALCOHOLIC(apiName = "Alcoholic"),
    NONALCOHOLIC(apiName = "Non alcoholic"),
    OPTALCOHOL(apiName = "Optional alcohol")
}