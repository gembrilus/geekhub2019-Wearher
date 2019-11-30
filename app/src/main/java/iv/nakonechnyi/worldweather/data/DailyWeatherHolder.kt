package iv.nakonechnyi.worldweather.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DailyWeatherHolder(
    val city: City,
    val list: List<WeatherConditions>
)