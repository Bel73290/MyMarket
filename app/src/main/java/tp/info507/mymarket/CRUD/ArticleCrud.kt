package td.info507.mymarket.crud

import android.content.ContentValues
import android.content.Context
import td.info507.mymarket.helper.DataBaseHelper
import td.info507.mymarket.modele.Article

class ArticleCrud(context: Context) {
    private val dbh = DataBaseHelper(context)

    fun createArticle(name: String): Long {
        val v = ContentValues().apply {
            put(Article.NAME, name)
        }

        val db = dbh.writableDatabase
        val res = db.insert(Article.TABLE, null, v)
        return res
    }


    fun getAll(): List<Article> {
        val db = dbh.readableDatabase
        val out = mutableListOf<Article>()
        db.query(Article.TABLE, null, null, null, null, null, "${Article.NAME} ASC").use { c ->
            while (c.moveToNext()) {
                out += Article(
                    id = c.getInt(c.getColumnIndexOrThrow(Article.ID)),
                    name = c.getString(c.getColumnIndexOrThrow(Article.NAME))
                )
            }
        }
        return out
    }

    fun getById(id: Int): Article? {
        val db = dbh.readableDatabase
        var result: Article? = null
        db.query(
            Article.TABLE, null,
            "${Article.ID}=?", arrayOf(id.toString()), null, null, null
        ).use { c ->
            if (c.moveToFirst()) {
                result = Article(
                    id = c.getInt(c.getColumnIndexOrThrow(Article.ID)),
                    name = c.getString(c.getColumnIndexOrThrow(Article.NAME))
                )
            }
        }
        return result
    }

    fun delete(id: Int): Int {
        val rows = dbh.writableDatabase.delete(
            Article.TABLE, "${Article.ID}=?", arrayOf(id.toString())
        )
        return rows
    }


}
