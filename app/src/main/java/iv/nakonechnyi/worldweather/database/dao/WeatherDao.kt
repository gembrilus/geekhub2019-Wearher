package iv.nakonechnyi.worldweather.database.dao

import android.content.ContentValues
import android.content.Context
import iv.nakonechnyi.worldweather.data.*
import iv.nakonechnyi.worldweather.database.entries.WeatherDbScheme.CityEntry
import iv.nakonechnyi.worldweather.database.entries.WeatherDbScheme.WeatherConditionsEntry

class WeatherDao(private val context: Context) {

    private val database get() = WeatherDbHelper(context).writableDatabase

    fun insert(dailyWeatherHolder: DailyWeatherHolder) {

        val cityId = database.insert(
            CityEntry.TABLE_NAME,
            null,
            getContentValuesOfCity(dailyWeatherHolder)
        )

        dailyWeatherHolder.list.forEach {
            database.insert(
                WeatherConditionsEntry.TABLE_NAME,
                null,
                getContentValuesOfWeather(it, cityId)
            )
        }
    }

    fun take(cityName: String): DailyWeatherHolder? {
        val weatherConditions = mutableListOf<WeatherConditions>()
        var dailyWeatherHolder: DailyWeatherHolder? = null
        database.rawQuery(SELECT_WEATHER_BY_CITY_NAME, arrayOf(cityName))
            .run(::WeatherCursorWrapper)
            .use {cursor ->
                if (cursor.count > 0) {
                    cursor.moveToFirst()
                    val city = cursor.getCity()
                    weatherConditions.add(cursor.getWeatherCondition())
                    while (cursor.moveToNext()) {
                        weatherConditions.add(cursor.getWeatherCondition())
                    }
                    dailyWeatherHolder = DailyWeatherHolder(
                        city = city,
                        list = weatherConditions
                    )
                }
            }
        return dailyWeatherHolder
    }

    fun replace(dailyWeatherHolder: DailyWeatherHolder){
        val index = database.delete(
            CityEntry.TABLE_NAME,
            "${CityEntry.COLUMN_NAME_CITY} LIKE ?",
            arrayOf(dailyWeatherHolder.city.name))

        repeat(dailyWeatherHolder.list.size) {
            database.delete(
                WeatherConditionsEntry.TABLE_NAME,
                "${WeatherConditionsEntry.COLUMN_NAME_CITY_ID} LIKE ?",
                arrayOf(index.toString())
            )
        }
        insert(dailyWeatherHolder)
    }

    fun hasEntry(dailyWeatherHolder: DailyWeatherHolder): Boolean{
        database.rawQuery(SELECT_CITY_BY_NAME, arrayOf(dailyWeatherHolder.city.name))
            .run(::WeatherCursorWrapper)
            .use {cursor ->
                return cursor.count > 0
            }
    }

    fun wasEntryChanged(dailyWeatherHolder: DailyWeatherHolder): Boolean {
        database.rawQuery(SELECT_CITY_BY_NAME, arrayOf(dailyWeatherHolder.city.name))
            .run(::WeatherCursorWrapper)
            .use { cursor ->
                cursor.moveToNext()
                return dailyWeatherHolder.hashCode() !=
                        cursor.getInt(cursor.getColumnIndex(CityEntry.COLUMN_HASHCODE))
            }
    }

    fun closeDatabase() = database.close()

    private fun getContentValuesOfCity(dailyWeatherHolder: DailyWeatherHolder) = ContentValues().apply {
        put(CityEntry.COLUMN_NAME_CITY,     dailyWeatherHolder.city.name)
        put(CityEntry.COLUMN_HASHCODE,      dailyWeatherHolder.hashCode())
        put(CityEntry.COLUMN_NAME_COUNTRY,  dailyWeatherHolder.city.country)
        put(CityEntry.COLUMN_NAME_LAT,      dailyWeatherHolder.city.coord["lat"])
        put(CityEntry.COLUMN_NAME_LON,      dailyWeatherHolder.city.coord["lon"])
    }

    private fun getContentValuesOfWeather(weatherCondition: WeatherConditions, cityId: Long) = ContentValues().apply {
        put(WeatherConditionsEntry.COLUMN_NAME_DT,           weatherCondition.dt)
        put(WeatherConditionsEntry.COLUMN_NAME_CITY_ID,      cityId)
        put(WeatherConditionsEntry.COLUMN_NAME_TEMP,         weatherCondition.main.temp)
        put(WeatherConditionsEntry.COLUMN_NAME_TEMP_MIN,     weatherCondition.main.temp_min)
        put(WeatherConditionsEntry.COLUMN_NAME_TEMP_MAX,     weatherCondition.main.temp_max)
        put(WeatherConditionsEntry.COLUMN_NAME_PRESSURE,     weatherCondition.main.pressure)
        put(WeatherConditionsEntry.COLUMN_NAME_SEA_LEVEL,    weatherCondition.main.sea_level)
        put(WeatherConditionsEntry.COLUMN_NAME_GROUND_LEVEL, weatherCondition.main.grnd_level)
        put(WeatherConditionsEntry.COLUMN_NAME_HUMIDITY,     weatherCondition.main.humidity)
        put(WeatherConditionsEntry.COLUMN_NAME_DESC,         weatherCondition.weather[0].description)
        put(WeatherConditionsEntry.COLUMN_NAME_ICON,         weatherCondition.weather[0].icon)
        put(WeatherConditionsEntry.COLUMN_NAME_CLOUDS,       weatherCondition.clouds?.get("all"))
        put(WeatherConditionsEntry.COLUMN_NAME_WIND_SPEED,   weatherCondition.wind["speed"])
        put(WeatherConditionsEntry.COLUMN_NAME_WIND_SPEED,   weatherCondition.wind["deg"])
        put(WeatherConditionsEntry.COLUMN_NAME_RAIN,         weatherCondition.rain?.get("3h"))
        put(WeatherConditionsEntry.COLUMN_NAME_SNOW,         weatherCondition.snow?.get("3h"))
    }
}