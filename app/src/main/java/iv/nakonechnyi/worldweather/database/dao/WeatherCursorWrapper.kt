package iv.nakonechnyi.worldweather.database.dao

import android.database.Cursor
import android.database.CursorWrapper
import iv.nakonechnyi.worldweather.data.*
import iv.nakonechnyi.worldweather.database.entries.WeatherDbScheme.CityEntry
import iv.nakonechnyi.worldweather.database.entries.WeatherDbScheme.WeatherConditionsEntry

class WeatherCursorWrapper(cursor: Cursor) : CursorWrapper(cursor) {

    fun getCity(): City {
        val cityName = getString(getColumnIndex(CityEntry.COLUMN_NAME_CITY))
        val countryName = getString(getColumnIndex(CityEntry.COLUMN_NAME_COUNTRY))
        val latitude = getDouble(getColumnIndex(CityEntry.COLUMN_NAME_LAT))
        val longitude = getDouble(getColumnIndex(CityEntry.COLUMN_NAME_LON))
        return City(
            name = cityName,
            coord = mapOf("lat" to latitude, "lon" to longitude),
            country = countryName
        )
    }

    fun getWeatherCondition(): WeatherConditions {
        val dt = getLong(getColumnIndex(WeatherConditionsEntry.COLUMN_NAME_DT))
        val temp = getDouble(getColumnIndex(WeatherConditionsEntry.COLUMN_NAME_TEMP))
        val tempMin = getDouble(getColumnIndex(WeatherConditionsEntry.COLUMN_NAME_TEMP_MIN))
        val tempMax = getDouble(getColumnIndex(WeatherConditionsEntry.COLUMN_NAME_TEMP_MAX))
        val pressure = getDouble(getColumnIndex(WeatherConditionsEntry.COLUMN_NAME_PRESSURE))
        val seaLevel = getDouble(getColumnIndex(WeatherConditionsEntry.COLUMN_NAME_SEA_LEVEL))
        val groundLevel = getDouble(getColumnIndex(WeatherConditionsEntry.COLUMN_NAME_GROUND_LEVEL))
        val humidity = getDouble(getColumnIndex(WeatherConditionsEntry.COLUMN_NAME_HUMIDITY))
        val weatherDescription = getString(getColumnIndex(WeatherConditionsEntry.COLUMN_NAME_DESC))
        val iconName = getString(getColumnIndex(WeatherConditionsEntry.COLUMN_NAME_ICON))
        val typeClouds = getType(getColumnIndex(WeatherConditionsEntry.COLUMN_NAME_CLOUDS))
        val clouds = if (typeClouds == Cursor.FIELD_TYPE_NULL) {
            getDouble(getColumnIndex(WeatherConditionsEntry.COLUMN_NAME_CLOUDS))
        } else null
        val windSpeed = getDouble(getColumnIndex(WeatherConditionsEntry.COLUMN_NAME_WIND_SPEED))
        val windDegrees = getDouble(getColumnIndex(WeatherConditionsEntry.COLUMN_NAME_WIND_DEGREES))
        val typeRain = getType(getColumnIndex(WeatherConditionsEntry.COLUMN_NAME_RAIN))
        val rain = if (typeRain == Cursor.FIELD_TYPE_NULL){
            getDouble(getColumnIndex(WeatherConditionsEntry.COLUMN_NAME_RAIN))
        } else null
        val typeSnow = getType(getColumnIndex(WeatherConditionsEntry.COLUMN_NAME_SNOW))
        val snow = if (typeSnow == Cursor.FIELD_TYPE_NULL){
            getDouble(getColumnIndex(WeatherConditionsEntry.COLUMN_NAME_SNOW))
        } else null

        return WeatherConditions(
            dt = dt,
            main = MainWeatherInfo(
                temp = temp,
                temp_min = tempMin,
                temp_max = tempMax,
                pressure = pressure,
                sea_level = seaLevel,
                grnd_level = groundLevel,
                humidity = humidity
            ),
            weather = listOf(
                Weather(
                    description = weatherDescription,
                    icon = iconName
                )
            ),
            wind = mapOf("speed" to windSpeed, "deg" to windDegrees),
            clouds = if (clouds == null) null else mapOf("all" to clouds),
            rain = if (rain == null) null else mapOf("3h" to rain),
            snow = if (snow == null) null else mapOf("3h" to snow)
        )
    }
}