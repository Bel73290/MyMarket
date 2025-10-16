package td.info507.mymarket.crud

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import td.info507.mymarket.helper.DataBaseHelper
import td.info507.mymarket.modele.Course

class CourseCrud(context: Context) {
    private val dbh = DataBaseHelper(context)

    fun createCourse(nom: String, date: String, prixInitial: Int): Long {
        val v = ContentValues().apply {
            put(Course.NOM, nom)
            put(Course.DATE, date)
            put(Course.PRIX_INITIAL, prixInitial)
            put(Course.ETAT, 0)
        }

        val db = dbh.writableDatabase
        val res = db.insert(Course.TABLE, null, v)
        return res
    }

    fun insert(c: Course): Long {
        val v = ContentValues().apply {
            put(Course.ID, c.id)
            put(Course.NOM, c.nom)
            put(Course.DATE, c.date)
            put(Course.PRIX_INITIAL, c.prix_initial)
            put(Course.ETAT, if (c.etat) 1 else 0)
        }
        val res = dbh.writableDatabase.insert(Course.TABLE, null, v)
        return res
    }

    fun update(c: Course): Int {
        val v = ContentValues().apply {
            put(Course.NOM, c.nom)
            put(Course.DATE, c.date)
            put(Course.PRIX_INITIAL, c.prix_initial)
            put(Course.ETAT, if (c.etat) 1 else 0)
        }
        val rows = dbh.writableDatabase.update(
            Course.TABLE, v, "${Course.ID}=?", arrayOf(c.id.toString())
        )
        return rows
    }

    fun delete(id: Int): Int {
        val rows = dbh.writableDatabase.delete(
            Course.TABLE, "${Course.ID}=?", arrayOf(id.toString())
        )
        return rows
    }

    fun getById(id: Int): Course? {
        val db = dbh.readableDatabase
        var result: Course? = null
        db.query(
            Course.TABLE, null,
            "${Course.ID}=?", arrayOf(id.toString()), null, null, null
        ).use { c ->
            if (c.moveToFirst()) {
                result = Course(
                    id = c.getInt(c.getColumnIndexOrThrow(Course.ID)),
                    nom = c.getString(c.getColumnIndexOrThrow(Course.NOM)),
                    date = c.getString(c.getColumnIndexOrThrow(Course.DATE)),
                    prix_initial = c.getInt(c.getColumnIndexOrThrow(Course.PRIX_INITIAL)),
                    etat = c.getInt(c.getColumnIndexOrThrow(Course.ETAT)) == 1
                )
            }
        }
        return result
    }

    fun getAll(): List<Course> {
        val db = dbh.readableDatabase
        val out = mutableListOf<Course>()
        db.query(Course.TABLE, null, null, null, null, null, "${Course.ID} DESC").use { c ->
            while (c.moveToNext()) {
                out += Course(
                    id = c.getInt(c.getColumnIndexOrThrow(Course.ID)),
                    nom = c.getString(c.getColumnIndexOrThrow(Course.NOM)),
                    date = c.getString(c.getColumnIndexOrThrow(Course.DATE)),
                    prix_initial = c.getInt(c.getColumnIndexOrThrow(Course.PRIX_INITIAL)),
                    etat = c.getInt(c.getColumnIndexOrThrow(Course.ETAT)) == 1
                )
            }
        }
        return out
    }

    fun setEtat(id: Int, finished: Boolean): Int {
        val v = ContentValues().apply { put(Course.ETAT, if (finished) 1 else 0) }
        val rows = dbh.writableDatabase.update(
            Course.TABLE, v, "${Course.ID}=?", arrayOf(id.toString())
        )
        return rows
    }
}
