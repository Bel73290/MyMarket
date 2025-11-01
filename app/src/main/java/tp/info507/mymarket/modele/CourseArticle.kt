package td.info507.mymarket.modele

data class CourseArticle(
    val courseId: Int,
    val articleId: Int,
    val price_final: Int,
    val checked: Boolean
) {
    companion object {
        const val TABLE = "CourseArticle"
        const val COURSE_ID = "course_id"
        const val ARTICLE_ID = "article_id"
        const val PRICE_FINAL  = "price_final"
        const val CHECKED = "checked"
    }
}
