package iv.nakonechnyi.worldweather.services

import android.app.IntentService
import android.content.Context
import android.content.Intent
import iv.nakonechnyi.worldweather.data.DailyWeatherHolder
import iv.nakonechnyi.worldweather.database.dao.WeatherDao
import iv.nakonechnyi.worldweather.etc.SPHolder
import iv.nakonechnyi.worldweather.etc.SP_FILE
import iv.nakonechnyi.worldweather.net.weatherservice.WeatherRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class WeatherService : IntentService(WeatherService::class.simpleName),
    Callback<DailyWeatherHolder> {

    private val ctx by lazy { applicationContext }
    private val weatherDao by lazy { WeatherDao.apply { context = ctx } }
    private val spHolder by lazy {
        SPHolder(
            ctx,
            ctx.getSharedPreferences(SP_FILE, Context.MODE_PRIVATE)
        )
    }

    override fun onHandleIntent(p0: Intent?) {
        synchronized(this) {
            try {
                    fetchWeatherForecast()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun fetchWeatherForecast() {
        WeatherRequest(ctx, spHolder.map, this).make()
    }

    override fun onFailure(call: Call<DailyWeatherHolder>, t: Throwable) {}

    override fun onResponse(
        call: Call<DailyWeatherHolder>,
        response: Response<DailyWeatherHolder>
    ) {
        if (response.isSuccessful) {
            val dailyWeatherHolder = response.body()
            if (dailyWeatherHolder != null) {
                if (!weatherDao.hasEntry(dailyWeatherHolder)) {
                    weatherDao.insert(dailyWeatherHolder)
                    ServiceHelper(ctx).notifyWeatherWasInserted()
                } else if (weatherDao.wasEntryChanged(dailyWeatherHolder)) {
                    weatherDao.replace(dailyWeatherHolder)
                    ServiceHelper(ctx).notifyWeatherWasChanged()
                }
            }
        }
    }

    override fun onBind(p0: Intent?) = null
}