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

        val res = dbh.writableDatabase.insert(Article.TABLE, null, v)
        return res
    }


    fun getAll(): List<Article> {
        val res = mutableListOf<Article>()
        dbh.readableDatabase.query(Article.TABLE, null, null, null, null, null, "${Article.NAME} ASC").use { c ->
            while (c.moveToNext()) {
                res += Article(
                    id = c.getInt(c.getColumnIndexOrThrow(Article.ID)),
                    name = c.getString(c.getColumnIndexOrThrow(Article.NAME))
                )
            }
        }
        return res
    }

    fun getById(id: Int): Article? {
        var res: Article? = null
        dbh.readableDatabase.query(
            Article.TABLE, null,
            "${Article.ID}=?", arrayOf(id.toString()), null, null, null
        ).use { c ->
            if (c.moveToFirst()) {
                res = Article(
                    id = c.getInt(c.getColumnIndexOrThrow(Article.ID)),
                    name = c.getString(c.getColumnIndexOrThrow(Article.NAME))
                )
            }
        }
        return res
    }

    fun delete(id: Int): Int {
        val res = dbh.writableDatabase.delete(Article.TABLE, "${Article.ID}=?", arrayOf(id.toString()))
        return res
    }
}
