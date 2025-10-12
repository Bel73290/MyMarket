package td.info507.mymarket.modele

data class Article(
    val name: String,
    val price_estime: Int,
    val checked: Boolean
) {
    companion object {
        const val NAME = "name"
        const val PRICE_ESTIME = "price_estime"
        const val CHECKED = "checked"
    }
}


