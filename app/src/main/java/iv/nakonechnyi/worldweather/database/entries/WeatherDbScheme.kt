package iv.nakonechnyi.worldweather.database.entries

import android.provider.BaseColumns

object WeatherDbScheme {

    object CityEntry: BaseColumns {

        const val TABLE_NAME = "city"
        const val COLUMN_HASHCODE = "hashcode"
        const val COLUMN_NAME_CITY = "name"
        const val COLUMN_NAME_COUNTRY = "country"
        const val COLUMN_NAME_LAT = "lat"
        const val COLUMN_NAME_LON = "lon"
    }

    object WeatherConditionsEntry : BaseColumns {

        const val TABLE_NAME = "weather_conditions"
        const val COLUMN_NAME_DT = "dt"
        const val COLUMN_NAME_CITY_ID = "city_id"
        const val COLUMN_NAME_TEMP = "temp"
        const val COLUMN_NAME_TEMP_MIN = "temp_min"
        const val COLUMN_NAME_TEMP_MAX = "temp_max"
        const val COLUMN_NAME_PRESSURE = "pressure"
        const val COLUMN_NAME_SEA_LEVEL = "sea_level"
        const val COLUMN_NAME_GROUND_LEVEL = "grnd_level"
        const val COLUMN_NAME_HUMIDITY = "humidity"
        const val COLUMN_NAME_DESC = "description"
        const val COLUMN_NAME_ICON = "icon"
        const val COLUMN_NAME_WIND_SPEED = "speed"
        const val COLUMN_NAME_WIND_DEGREES = "deg"
        const val COLUMN_NAME_CLOUDS = "clouds"
        const val COLUMN_NAME_RAIN = "rain"
        const val COLUMN_NAME_SNOW = "snow"

    }
}