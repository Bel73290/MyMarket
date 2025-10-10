package td.info507.mymarket.storage

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import td.info507.mymarket.helper.DataBaseHelper
import td.info507.mymarket.modele.Course

class CourseDataBaseStorage(context: Context) {

    private val dbHelper = DataBaseHelper(context)

    fun insertCourse(nom: String, date: String, prix: Int?, lieu: String, etat: Boolean): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(Course.NOM, nom)
            put(Course.DATE, date)
            put(Course.PRIX_INITIAL, prix)
            put(Course.LIEU, lieu)
            put(Course.ETAT, etat)
        }

        return db.insert("Course", null, values)
    }

    fun getCourses(): List<Course> {
        val courses = mutableListOf<Course>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Course", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
                val nom = cursor.getString(cursor.getColumnIndexOrThrow(Course.NOM))
                val date = cursor.getString(cursor.getColumnIndexOrThrow(Course.DATE))
                val prixInitial = cursor.getInt(cursor.getColumnIndexOrThrow(Course.PRIX_INITIAL))
                val lieu = cursor.getString(cursor.getColumnIndexOrThrow(Course.LIEU))
                val etat = cursor.getInt(cursor.getColumnIndexOrThrow(Course.ETAT)) > 0

                val course = Course(id, nom, date, prixInitial, lieu, etat)
                courses.add(course)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return courses
    }




}
//
//    private val dbHelper = DataBaseHelper(context)
//
//    private fun articlesToJson(articles: List<Article>): String {
//        val arr = JSONArray()
//        for (a in articles) {
//            arr.put(
//                JSONObject()
//                    .put(Article.NAME, a.name)
//                    .put(Article.PRICE_ESTIME, a.price_estime)
//                    .put(Article.PRICE_FINAL, a.price_final)
//                    .put(Article.CHECKED, a.checked)
//            )
//        }
//        return arr.toString()
//    }
//
//    private fun jsonToArticles(json: String?): List<Article> {
//        if (json.isNullOrBlank()) return emptyList()
//        val arr = JSONArray(json)
//        val list = ArrayList<Article>()
//        for (i in 0 until arr.length()) {
//            val o = arr.getJSONObject(i)
//            list += Article(
//                name = o.getString(Article.NAME),
//                price_estime = o.getInt(Article.PRICE_ESTIME),
//                price_final = o.getInt(Article.PRICE_FINAL),
//                checked = o.optBoolean(Article.CHECKED, false)
//            )
//        }
//        return list
//    }
//
//    fun insert(course: Course, articles: List<Article>): Long {
//        val db = dbHelper.writableDatabase
//        val values = ContentValues().apply {
//            put(BaseColumns._ID, course.id)
//            put(Course.NOM, course.nom)
//            put(Course.DATE, course.date)
//            put(Course.PRIX_INITIAL, course.prix_initial)
//            put(Course.LIEU, course.lieu)
//            put(Course.ETAT, if (course.etat) 1 else 0)
//            put("articles", articlesToJson(articles))
//        }
//        return db.insert("Course", null, values)
//    }
//
//    fun update(course: Course, articles: List<Article>): Int {
//        val db = dbHelper.writableDatabase
//        val values = ContentValues().apply {
//            put(Course.NOM, course.nom)
//            put(Course.DATE, course.date)
//            put(Course.PRIX_INITIAL, course.prix_initial)
//            put(Course.LIEU, course.lieu)
//            put(Course.ETAT, if (course.etat) 1 else 0)
//            put("articles", articlesToJson(articles))
//        }
//        return db.update("Course", values, "${BaseColumns._ID}=?", arrayOf(course.id.toString()))
//    }
//
//    fun delete(id: Int): Int {
//        val db = dbHelper.writableDatabase
//        return db.delete("Course", "${BaseColumns._ID}=?", arrayOf(id.toString()))
//    }
//
//    fun findById(id: Int): Pair<Course, List<Article>>? {
//        val db = dbHelper.readableDatabase
//        db.query(
//            "Course",
//            null,
//            "${BaseColumns._ID}=?",
//            arrayOf(id.toString()),
//            null, null, null
//        ).use { c ->
//            return if (c.moveToFirst()) cursorToCourseWithArticles(c) else null
//        }
//    }
//
//
//    fun getAllCourses(): List<Course> {
//        val db = dbHelper.readableDatabase
//        val courses = mutableListOf<Course>()
//
//        db.query(
//            "Course",
//            null, null, null, null, null, null
//        ).use { cursor ->
//            if (cursor.moveToFirst()) {
//                do {
//                    val id = cursor.getInt(cursor.getColumnIndexOrThrow(android.provider.BaseColumns._ID))
//                    val nom = cursor.getString(cursor.getColumnIndexOrThrow(Course.NOM))
//                    val date = cursor.getString(cursor.getColumnIndexOrThrow(Course.DATE))
//                    val prixInitial = cursor.getInt(cursor.getColumnIndexOrThrow(Course.PRIX_INITIAL))
//                    val lieu = cursor.getString(cursor.getColumnIndexOrThrow(Course.LIEU))
//                    val etat = cursor.getInt(cursor.getColumnIndexOrThrow(Course.ETAT)) == 1
//
//                    courses += Course(id, nom, date, prixInitial, lieu, etat)
//                } while (cursor.moveToNext())
//            }
//        }
//        return courses
//    }
//
//
//
//
//    fun findAll(): List<Pair<Course, List<Article>>> {
//        val db = dbHelper.readableDatabase
//        db.query("Course", null, null, null, null, null, "${BaseColumns._ID} DESC")
//            .use { c ->
//                val list = ArrayList<Pair<Course, List<Article>>>()
//                while (c.moveToNext()) list += cursorToCourseWithArticles(c)
//                return list
//            }
//    }
//
//    private fun cursorToCourseWithArticles(c: Cursor): Pair<Course, List<Article>> {
//        val id = c.getInt(c.getColumnIndexOrThrow(android.provider.BaseColumns._ID))
//        val nom = c.getString(c.getColumnIndexOrThrow(Course.NOM))
//        val date = c.getString(c.getColumnIndexOrThrow(Course.DATE))
//        val prixInit = c.getInt(c.getColumnIndexOrThrow(Course.PRIX_INITIAL))
//        val lieu = c.getString(c.getColumnIndexOrThrow(Course.LIEU))
//        val etat = c.getInt(c.getColumnIndexOrThrow(Course.ETAT)) == 1
//        val json = c.getString(c.getColumnIndexOrThrow("articles"))
//        return Course(id, nom, date, prixInit, lieu, etat) to jsonToArticles(json)
//    }
//
//
//    fun calculerBudgetEstime(articles: List<Article>): Int =
//        articles.sumOf { it.price_estime }
//
//    fun calculerBudgetFinal(articles: List<Article>): Int =
//        articles.sumOf { it.price_final }
//
//    fun terminerCourse(articles: List<Article>): List<Article> =
//        articles.map { if (it.checked) it else it.copy(price_final = 0) }
//}
//
