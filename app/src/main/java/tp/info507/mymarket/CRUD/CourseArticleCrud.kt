package td.info507.mymarket.crud

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import td.info507.mymarket.helper.DataBaseHelper
import td.info507.mymarket.modele.Article
import td.info507.mymarket.modele.Course
import td.info507.mymarket.modele.CourseArticle

class CourseArticleCrud(context: Context) {
    private val dbh = DataBaseHelper(context)
    private val articleCrud = ArticleCrud(context)


    //Crée une nouvelle ligne pour la course et un Article avec son nom
    fun createItem(courseId: Int, name: String, priceEstime: Int): Long {
        // Vérifie si l'article existe déjà
        val existingArticle = articleCrud.getAll().find { it.name == name }
        val articleId = if (existingArticle != null) {
            existingArticle.id
        } else {
            articleCrud.createArticle(name).toInt()
        }

        val v = ContentValues().apply {
            put(CourseArticle.COURSE_ID, courseId)
            put(CourseArticle.ARTICLE_ID, articleId)
            put(CourseArticle.PRICE_ESTIME, priceEstime)
            put(CourseArticle.PRICE_FINAL, priceEstime)
            put(CourseArticle.CHECKED, 0)
        }

        return dbh.writableDatabase.insertWithOnConflict(
            CourseArticle.TABLE, null, v, SQLiteDatabase.CONFLICT_REPLACE
        )
    }



    // met a jour le prix final
    fun setFinalPrice(courseId: Int, articleId: Int, price: Int): Int {
        val v = ContentValues().apply { put(CourseArticle.PRICE_FINAL, price) }
        val res = dbh.writableDatabase.update(
            CourseArticle.TABLE, v,
            "${CourseArticle.COURSE_ID}=? AND ${CourseArticle.ARTICLE_ID}=?",
            arrayOf(courseId.toString(), articleId.toString())
        )
        return res
    }

    // Coche/décoche Si on coche et price_final == 0 → price_final = price_estime
    fun toggleChecked(courseId: Int, articleId: Int): Int {

        var curChecked = 0
        var curFinal = 0
        var curEstime = 0
        dbh.writableDatabase.query(
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
        val res = dbh.writableDatabase.update(
            CourseArticle.TABLE, v,
            "${CourseArticle.COURSE_ID}=? AND ${CourseArticle.ARTICLE_ID}=?",
            arrayOf(courseId.toString(), articleId.toString())
        )

        if (newChecked == 1 && curFinal == 0) {
            val v2 = ContentValues().apply { put(CourseArticle.PRICE_FINAL, curEstime) }
            dbh.writableDatabase.update(
                CourseArticle.TABLE, v2,
                "${CourseArticle.COURSE_ID}=? AND ${CourseArticle.ARTICLE_ID}=?",
                arrayOf(courseId.toString(), articleId.toString())
            )
        }
        return res
    }

    //Supprimer une ligne article
    fun removeItem(courseId: Int, articleId: Int): Int {
        val res = dbh.writableDatabase.delete(
            CourseArticle.TABLE,
            "${CourseArticle.COURSE_ID}=? AND ${CourseArticle.ARTICLE_ID}=?",
            arrayOf(courseId.toString(), articleId.toString())
        )
        return res
    }


     //Liste les lignes d’une course.
     //Renvoie Pair(CourseArticle, nomArticle) pour rester fidèle au modèle
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

        val res = mutableListOf<Pair<CourseArticle, String>>()
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
                res += Pair(item, articleName)
            }
        }
         Log.d("DebugGetItems", "courseId=$courseId, articles récupérés=${res.size}")
         res.forEach { (_, name) ->
             Log.d("DebugGetItems", "Article: $name")
         }
        return res
    }

    // Total courrant quand on coche sans que la course soit finit
    fun budgetFinalDuring(courseId: Int): Int {
        val sql = "SELECT IFNULL(SUM(${CourseArticle.PRICE_FINAL}),0) FROM ${CourseArticle.TABLE} WHERE ${CourseArticle.COURSE_ID}=? AND ${CourseArticle.CHECKED}=1"
        var res = 0
        dbh.readableDatabase.rawQuery(sql, arrayOf(courseId.toString())).use { c ->
            if (c.moveToFirst()) {
                res = c.getInt(0)
            }
        }
        return res
    }

    // calcul du total final de la courseArticle
    fun budgetFinalAfter(courseId: Int): Int {
        val sql = "SELECT IFNULL(SUM(${CourseArticle.PRICE_FINAL}),0) FROM ${CourseArticle.TABLE} WHERE ${CourseArticle.COURSE_ID}=?"
        var res = 0
        dbh.readableDatabase.rawQuery(sql, arrayOf(courseId.toString())).use { c ->
            if (c.moveToFirst()) {
                res = c.getInt(0)
            }
        }
        return res
    }

    // Termine la course met price_final=0 pour les non coché puis etat=1 sur la CourseArticle
    fun finishCourse(courseId: Int) {
        dbh.writableDatabase.beginTransaction()
        try {
            val v = ContentValues().apply { put(CourseArticle.PRICE_FINAL, 0) }
            dbh.writableDatabase.update(
                CourseArticle.TABLE, v,
                "${CourseArticle.COURSE_ID}=? AND ${CourseArticle.CHECKED}=0",
                arrayOf(courseId.toString())
            )
            val v2 = ContentValues().apply { put(Course.ETAT, 1) }
            dbh.writableDatabase.update(Course.TABLE, v2, "${Course.ID}=?", arrayOf(courseId.toString()))
            dbh.writableDatabase.setTransactionSuccessful()
        } finally {
            dbh.writableDatabase.endTransaction()
        }
    }

    //compte le nombre d'article dans une course
    fun nbArticleCourse(courseId: Int): Int {
        var res = 0
        val cursor = dbh.readableDatabase.rawQuery(
            "SELECT COUNT(*) FROM ${CourseArticle.TABLE} WHERE ${CourseArticle.COURSE_ID} = ?",
            arrayOf(courseId.toString())
        )

        if (cursor.moveToFirst()) {
            res = cursor.getInt(0)
        }
        cursor.close()
        return res
    }

}
