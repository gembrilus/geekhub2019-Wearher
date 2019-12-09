package iv.nakonechnyi.worldweather.database.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import iv.nakonechnyi.worldweather.database.extensions.execSQL

class WeatherDbHelper(context: Context) : SQLiteOpenHelper(context,
    DB_NAME, null,
    DB_VERSION
){
    override fun onCreate(db: SQLiteDatabase?) {

        db?.execSQL(
            CREATE_CITY_TABLE,
            CREATE_WEATHER_CONDITIONS_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(REMOVE_ALL_TABLES)
        onCreate(db)
    }

    companion object{
        private const val DB_NAME = "weather.db"
        private const val DB_VERSION = 1
    }
}