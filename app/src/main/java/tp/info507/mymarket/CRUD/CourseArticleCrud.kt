package td.info507.mymarket.crud

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import td.info507.mymarket.helper.DataBaseHelper
import td.info507.mymarket.modele.Article
import td.info507.mymarket.modele.Course
import td.info507.mymarket.modele.CourseArticle

class CourseArticleCrud(context: Context) {
    private val dbh = DataBaseHelper(context)
    private val articleCrud = ArticleCrud(context)

    /** Crée une nouvelle ligne pour la course (crée aussi un Article avec ce name). */
    fun createItem(courseId: Int, name: String, priceEstime: Int): Long {
        // 1) Créer l'article (doublons autorisés)
        val articleIdLong = articleCrud.createArticle(name)
        val articleId = articleIdLong.toInt()

        // 2) Créer la ligne CourseArticle
        val v = ContentValues().apply {
            put(CourseArticle.COURSE_ID, courseId)
            put(CourseArticle.ARTICLE_ID, articleId)
            put(CourseArticle.PRICE_ESTIME, priceEstime)
            put(CourseArticle.PRICE_FINAL, priceEstime)
            put(CourseArticle.CHECKED, 0)
        }
        val res = dbh.writableDatabase.insertWithOnConflict(
            CourseArticle.TABLE, null, v, SQLiteDatabase.CONFLICT_REPLACE
        )
        return res
    }

    /** Met à jour le prix final d’un article de la course. */
    fun setFinalPrice(courseId: Int, articleId: Int, price: Int): Int {
        val v = ContentValues().apply { put(CourseArticle.PRICE_FINAL, price) }
        val rows = dbh.writableDatabase.update(
            CourseArticle.TABLE, v,
            "${CourseArticle.COURSE_ID}=? AND ${CourseArticle.ARTICLE_ID}=?",
            arrayOf(courseId.toString(), articleId.toString())
        )
        return rows
    }

    /** Coche/décoche. Si on coche et price_final == 0 → price_final = price_estime. */
    fun toggleChecked(courseId: Int, articleId: Int): Int {
        val db = dbh.writableDatabase

        var curChecked = 0
        var curFinal = 0
        var curEstime = 0
        db.query(
            CourseArticle.TABLE,
            arrayOf(CourseArticle.CHECKED, CourseArticle.PRICE_FINAL, CourseArticle.PRICE_ESTIME),
            "${CourseArticle.COURSE_ID}=? AND ${CourseArticle.ARTICLE_ID}=?",
            arrayOf(courseId.toString(), articleId.toString()),
            null, null, null
        ).use { c ->
            if (c.moveToFirst()) {
                curChecked = c.getInt(0)
                curFinal = c.getInt(1)
                curEstime = c.getInt(2)
            }
        }

        val newChecked = if (curChecked == 1) 0 else 1
        val v = ContentValues().apply { put(CourseArticle.CHECKED, newChecked) }
        val rows = db.update(
            CourseArticle.TABLE, v,
            "${CourseArticle.COURSE_ID}=? AND ${CourseArticle.ARTICLE_ID}=?",
            arrayOf(courseId.toString(), articleId.toString())
        )

        if (newChecked == 1 && curFinal == 0) {
            val v2 = ContentValues().apply { put(CourseArticle.PRICE_FINAL, curEstime) }
            db.update(
                CourseArticle.TABLE, v2,
                "${CourseArticle.COURSE_ID}=? AND ${CourseArticle.ARTICLE_ID}=?",
                arrayOf(courseId.toString(), articleId.toString())
            )
        }
        return rows
    }

    /** Supprime une ligne (un article) de la course. */
    fun removeItem(courseId: Int, articleId: Int): Int {
        val rows = dbh.writableDatabase.delete(
            CourseArticle.TABLE,
            "${CourseArticle.COURSE_ID}=? AND ${CourseArticle.ARTICLE_ID}=?",
            arrayOf(courseId.toString(), articleId.toString())
        )
        return rows
    }

    /**
     * Liste les lignes d’une course.
     * Renvoie Pair(CourseArticle, nomArticle) pour rester fidèle au modèle (pas de champ name dans CourseArticle).
     */
    fun getItems(courseId: Int): List<Pair<CourseArticle, String>> {
        val sql = """
            SELECT ca.${CourseArticle.ARTICLE_ID} AS article_id,
                   a.${Article.NAME} AS name,
                   ca.${CourseArticle.PRICE_ESTIME},
                   ca.${CourseArticle.PRICE_FINAL},
                   ca.${CourseArticle.CHECKED}
            FROM ${CourseArticle.TABLE} ca
            JOIN ${Article.TABLE} a
              ON a.${Article.ID} = ca.${CourseArticle.ARTICLE_ID}
            WHERE ca.${CourseArticle.COURSE_ID}=?
            ORDER BY a.${Article.NAME} ASC
        """.trimIndent()

        val out = mutableListOf<Pair<CourseArticle, String>>()
        dbh.readableDatabase.rawQuery(sql, arrayOf(courseId.toString())).use { c ->
            while (c.moveToNext()) {
                val item = CourseArticle(
                    courseId = courseId,
                    articleId = c.getInt(c.getColumnIndexOrThrow("article_id")),
                    price_estime = c.getInt(c.getColumnIndexOrThrow(CourseArticle.PRICE_ESTIME)),
                    price_final  = c.getInt(c.getColumnIndexOrThrow(CourseArticle.PRICE_FINAL)),
                    checked      = c.getInt(c.getColumnIndexOrThrow(CourseArticle.CHECKED)) == 1
                )
                val articleName = c.getString(c.getColumnIndexOrThrow("name"))
                out += Pair(item, articleName)
            }
        }
        return out
    }

    /** Total courant: somme des price_final des lignes COCHÉES. */
    fun budgetFinalDuring(courseId: Int): Int {
        val sql = "SELECT IFNULL(SUM(${CourseArticle.PRICE_FINAL}),0) FROM ${CourseArticle.TABLE} WHERE ${CourseArticle.COURSE_ID}=? AND ${CourseArticle.CHECKED}=1"
        var total = 0
        dbh.readableDatabase.rawQuery(sql, arrayOf(courseId.toString())).use { c ->
            if (c.moveToFirst()) {
                total = c.getInt(0)
            }
        }
        return total
    }

    /** Total final après validation (toutes les lignes, les non cochées seront mises à 0 par finishCourse). */
    fun budgetFinalAfter(courseId: Int): Int {
        val sql = "SELECT IFNULL(SUM(${CourseArticle.PRICE_FINAL}),0) FROM ${CourseArticle.TABLE} WHERE ${CourseArticle.COURSE_ID}=?"
        var total = 0
        dbh.readableDatabase.rawQuery(sql, arrayOf(courseId.toString())).use { c ->
            if (c.moveToFirst()) {
                total = c.getInt(0)
            }
        }
        return total
    }

    /** Termine la course : met price_final=0 pour les non cochés, puis etat=1 sur Course. */
    fun finishCourse(courseId: Int) {
        val db = dbh.writableDatabase
        db.beginTransaction()
        try {
            val v = ContentValues().apply { put(CourseArticle.PRICE_FINAL, 0) }
            db.update(
                CourseArticle.TABLE, v,
                "${CourseArticle.COURSE_ID}=? AND ${CourseArticle.CHECKED}=0",
                arrayOf(courseId.toString())
            )
            val v2 = ContentValues().apply { put(Course.ETAT, 1) }
            db.update(Course.TABLE, v2, "${Course.ID}=?", arrayOf(courseId.toString()))
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }
}
