package iv.nakonechnyi.worldweather.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DailyWeatherHolder(
    val city: City,
    val list: List<WeatherConditions>
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DailyWeatherHolder

        if (city != other.city) return false
        if (list != other.list) return false

        return true
    }

    override fun hashCode(): Int {
        var result = city.hashCode()
        result = 31 * result + list.hashCode()
        return result
    }
}