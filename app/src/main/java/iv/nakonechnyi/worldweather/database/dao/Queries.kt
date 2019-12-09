package iv.nakonechnyi.worldweather.database.dao

import android.provider.BaseColumns
import iv.nakonechnyi.worldweather.database.entries.WeatherDbScheme.CityEntry
import iv.nakonechnyi.worldweather.database.entries.WeatherDbScheme.WeatherConditionsEntry

internal const val CREATE_CITY_TABLE =
    "CREATE TABLE ${CityEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY, " +
            "${CityEntry.COLUMN_HASHCODE} INTEGER, " +
            "${CityEntry.COLUMN_NAME_CITY} TEXT, " +
            "${CityEntry.COLUMN_NAME_COUNTRY} TEXT, " +
            "${CityEntry.COLUMN_NAME_LAT} REAL, " +
            "${CityEntry.COLUMN_NAME_LON} REAL" +
            ")"

internal const val CREATE_WEATHER_CONDITIONS_TABLE =
    "CREATE TABLE ${WeatherConditionsEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY, " +
            "${WeatherConditionsEntry.COLUMN_NAME_DT} NUMERIC, " +
            "${WeatherConditionsEntry.COLUMN_NAME_CITY_ID} INTEGER, " +
            "${WeatherConditionsEntry.COLUMN_NAME_TEMP} REAL, " +
            "${WeatherConditionsEntry.COLUMN_NAME_TEMP_MIN} REAL, " +
            "${WeatherConditionsEntry.COLUMN_NAME_TEMP_MAX} REAL, " +
            "${WeatherConditionsEntry.COLUMN_NAME_PRESSURE} REAL, " +
            "${WeatherConditionsEntry.COLUMN_NAME_SEA_LEVEL} REAL, " +
            "${WeatherConditionsEntry.COLUMN_NAME_GROUND_LEVEL} REAL, " +
            "${WeatherConditionsEntry.COLUMN_NAME_HUMIDITY} REAL, " +
            "${WeatherConditionsEntry.COLUMN_NAME_DESC} TEXT, " +
            "${WeatherConditionsEntry.COLUMN_NAME_ICON} TEXT, " +
            "${WeatherConditionsEntry.COLUMN_NAME_WIND_SPEED} REAL, " +
            "${WeatherConditionsEntry.COLUMN_NAME_WIND_DEGREES} REAL, " +
            "${WeatherConditionsEntry.COLUMN_NAME_CLOUDS} REAL, " +
            "${WeatherConditionsEntry.COLUMN_NAME_RAIN} REAL, " +
            "${WeatherConditionsEntry.COLUMN_NAME_SNOW} REAL" +
            ")"

internal const val REMOVE_ALL_TABLES =
            "DROP TABLE IF EXISTS ${CityEntry.TABLE_NAME};" +
            "DROP TABLE IF EXISTS ${WeatherConditionsEntry.TABLE_NAME}"

internal const val SELECT_WEATHER_BY_CITY_NAME =
    "SELECT * FROM ${CityEntry.TABLE_NAME} " +
            "INNER JOIN ${WeatherConditionsEntry.TABLE_NAME} " +
            "ON ${CityEntry.TABLE_NAME}.${BaseColumns._ID} = ${WeatherConditionsEntry.COLUMN_NAME_CITY_ID} " +
            "WHERE ${CityEntry.COLUMN_NAME_CITY} = ?"

internal const val SELECT_CITY_BY_NAME =
    "SELECT * FROM ${CityEntry.TABLE_NAME} WHERE ${CityEntry.COLUMN_NAME_CITY} = ?"