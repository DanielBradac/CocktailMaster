package cz.bradacd.cocktailmaster.datasource.displayable

data class DisplayableDrink (
    val source: String,
    val id: String,
    val name: String,
    val thumbImgSrc: String?
) {
    override fun equals(other: Any?): Boolean {
        if (other is DisplayableDrink) {
            return other.id == id
        }
        return false
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
