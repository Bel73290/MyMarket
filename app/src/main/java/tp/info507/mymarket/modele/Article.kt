package td.info507.mymarket.modele

data class Article(
    val id: Int,
    val name: String
) {
    companion object {
        const val TABLE = "Article"
        const val ID = "id"
        const val NAME = "name"
    }
}


