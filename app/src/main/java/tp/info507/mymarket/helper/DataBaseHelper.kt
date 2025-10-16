package td.info507.mymarket.helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import td.info507.mymarket.modele.Article
import td.info507.mymarket.modele.Course
import td.info507.mymarket.modele.CourseArticle

class DataBaseHelper(context: Context) : SQLiteOpenHelper(context, "mymarket.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE ${Course.TABLE} (" +
                    "${Course.ID} INTEGER," +
                    "${Course.NOM} TEXT," +
                    "${Course.DATE} TEXT," +
                    "${Course.PRIX_INITIAL} INTEGER," +
                    "${Course.ETAT} INTEGER," +
                    "PRIMARY KEY(${Course.ID})" +
                    ")"
        )

        db.execSQL(
            "CREATE TABLE ${Article.TABLE} (" +
                    "${Article.ID} INTEGER," +
                    "${Article.NAME} TEXT UNIQUE," +
                    "PRIMARY KEY(${Article.ID})" +
                    ")"
        )

        db.execSQL(
            "CREATE TABLE ${CourseArticle.TABLE} (" +
                    "${CourseArticle.COURSE_ID} INTEGER," +
                    "${CourseArticle.ARTICLE_ID} INTEGER," +
                    "${CourseArticle.PRICE_ESTIME} INTEGER," +
                    "${CourseArticle.PRICE_FINAL} INTEGER," +
                    "${CourseArticle.CHECKED} INTEGER," +
                    "PRIMARY KEY(${CourseArticle.COURSE_ID}, ${CourseArticle.ARTICLE_ID})," +
                    "FOREIGN KEY(${CourseArticle.COURSE_ID}) REFERENCES ${Course.TABLE}(${Course.ID})," +
                    "FOREIGN KEY(${CourseArticle.ARTICLE_ID}) REFERENCES ${Article.TABLE}(${Article.ID})" +
                    ")"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${CourseArticle.TABLE}")
        db.execSQL("DROP TABLE IF EXISTS ${Article.TABLE}")
        db.execSQL("DROP TABLE IF EXISTS ${Course.TABLE}")
        onCreate(db)
    }
}

