package td.info507.mymarket.helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import td.info507.mymarket.modele.Course

class DataBaseHelper(context: Context) : SQLiteOpenHelper(context, "mymarket.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE Course (" +
                    "${BaseColumns._ID} INTEGER," +
                    "${Course.NOM} TEXT," +
                    "${Course.DATE} TEXT," +
                    "${Course.PRIX_INITIAL} INTEGER," +
                    "${Course.LIEU} TEXT," +
                    "${Course.ETAT} INTEGER," +
                    "articles TEXT," +
                    "PRIMARY KEY(${BaseColumns._ID})" +
                    ")"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}
}
