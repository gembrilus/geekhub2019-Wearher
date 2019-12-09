package iv.nakonechnyi.worldweather.services

import android.app.IntentService
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import iv.nakonechnyi.worldweather.data.DailyWeatherHolder
import iv.nakonechnyi.worldweather.database.dao.WeatherDao
import iv.nakonechnyi.worldweather.etc.*
import iv.nakonechnyi.worldweather.etc.BROADCAST_ACTION_FILTER
import iv.nakonechnyi.worldweather.etc.SERVICE_STATUS
import iv.nakonechnyi.worldweather.etc.SP_FILE
import iv.nakonechnyi.worldweather.net.weatherservice.WeatherRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.UnknownHostException


class WeatherService : IntentService(WeatherService::class.simpleName),
    Callback<DailyWeatherHolder> {

    private val ctx by lazy { applicationContext }
    private val weatherDao by lazy { WeatherDao(ctx) }
    private val spHolder by lazy {
        SPHolder(
            ctx,
            ctx.getSharedPreferences(SP_FILE, Context.MODE_PRIVATE)
        )
    }

    override fun onHandleIntent(p0: Intent?) {

        sendBroadcast(Intent(BROADCAST_ACTION_FILTER).apply {
            putExtra(SERVICE_STATUS, Status.START)
        })

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

    override fun onFailure(call: Call<DailyWeatherHolder>, t: Throwable) {
        (ctx.getSystemService(Service.CONNECTIVITY_SERVICE) as ConnectivityManager).run {
            if (activeNetwork == null || t is UnknownHostException) {
                sendBroadcast(Intent(BROADCAST_ACTION_FILTER).apply {
                    putExtra(SERVICE_STATUS, Status.NETWORK_ERROR)
                })
            }
            if (call.isExecuted) {
                sendBroadcast(Intent(BROADCAST_ACTION_FILTER).apply {
                    putExtra(SERVICE_STATUS, Status.FAILURE)
                })
            }
        }
    }

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

                sendBroadcast(Intent(BROADCAST_ACTION_FILTER).apply {
                    putExtra(SERVICE_STATUS, Status.UPDATE)
                })
            }
        } else {
            sendBroadcast(Intent(BROADCAST_ACTION_FILTER).apply {
                putExtra(SERVICE_STATUS, Status.ON_RESPONSE_ERROR)
                putExtra(SERVICE_ERROR_CODE, response.code())
            })
        }
    }

    override fun onBind(p0: Intent?) = null

    interface ServiceStatusListener {
        fun start()
        fun update()
        fun stop()
        fun onServiceError(status: Status)
        fun onServiceResponseError(code: Int)
    }
}