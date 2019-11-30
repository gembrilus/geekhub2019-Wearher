package iv.nakonechnyi.worldweather.net

import iv.nakonechnyi.worldweather.data.DailyWeatherHolder
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface WeatherClient {
    @GET("data/2.5/forecast")
    fun getWeatherForecast(@QueryMap options: Map<String, String>): Call<DailyWeatherHolder>
//    fun getWeatherMap(@QueryMap options: Map<String, String>): Call<WeatherMap>
}