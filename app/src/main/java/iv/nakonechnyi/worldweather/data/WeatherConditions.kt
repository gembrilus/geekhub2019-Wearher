package iv.nakonechnyi.worldweather.data

data class WeatherConditions (
    val dt: Long,
    val main: MainWeatherInfo,
    val weather: List<Weather>,
    val wind: Map<String, Double>,
    val clouds: Map<String, Double>?,
    val rain: Map<String, Double>?,
    val snow: Map<String, Double>?
)

data class MainWeatherInfo(
    val temp: Double,
    val temp_min: Double = temp,
    val temp_max: Double = temp,
    val pressure: Double,
    val sea_level: Double = pressure,
    val grnd_level: Double = pressure,
    val humidity: Double
)