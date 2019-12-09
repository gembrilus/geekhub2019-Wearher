package iv.nakonechnyi.worldweather.database.extensions

import android.database.sqlite.SQLiteDatabase

fun SQLiteDatabase.execSQL(vararg queries: String) = queries.forEach { execSQL(it) }