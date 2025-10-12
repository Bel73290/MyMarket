package td.info507.mymarket.helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import td.info507.mymarket.modele.Article
import td.info507.mymarket.modele.Course

class DataBaseHelper(context: Context) : SQLiteOpenHelper(context, "mymarket2.db", null, 1) {


        override fun onCreate(db: SQLiteDatabase) {
            // Table Course
            db.execSQL(
                "CREATE TABLE Course (" +
                        "${BaseColumns._ID} INTEGER," +
                        "${Course.NOM} TEXT," +
                        "${Course.DATE} TEXT," +
                        "${Course.PRIX_INITIAL} INTEGER," +
                        "${Course.PRIX_FINAL} INTEGER," +
                        "${Course.ETAT} BOOLEAN," +
                        "PRIMARY KEY(${BaseColumns._ID})" +
                        ")"
            )

            // Table Articles avec clé secondaire vers Course
            db.execSQL(
                "CREATE TABLE Articles (" +
                        "${BaseColumns._ID} INTEGER," +
                        "${Article.NAME} TEXT," +
                        "${Article.PRICE_ESTIME} INTEGER," +
                        "${Article.CHECKED} BOOLEAN," +
                        "course_id INTEGER," +
                        "PRIMARY KEY(${BaseColumns._ID})," +
                        "FOREIGN KEY(course_id) REFERENCES Course(${BaseColumns._ID})" +
                        ")"
            )
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}
    }
