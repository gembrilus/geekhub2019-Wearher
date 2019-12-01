package iv.nakonechnyi.worldweather.ui.model

import android.app.Application
import android.app.Service
import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import iv.nakonechnyi.worldweather.data.*
import iv.nakonechnyi.worldweather.etc.SPHolder
import iv.nakonechnyi.worldweather.etc.SP_FILE
import iv.nakonechnyi.worldweather.net.errors.FailureExceptionHandler
import iv.nakonechnyi.worldweather.net.errors.ResponseExceptionHandler
import iv.nakonechnyi.worldweather.net.weatherservice.CallbackWrapper
import iv.nakonechnyi.worldweather.net.weatherservice.WeatherRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.UnknownHostException

class WeatherModel(application: Application) :
    AndroidViewModel(application),
    Callback<DailyWeatherHolder> {

    val data = MutableLiveData<DailyWeatherHolder?>()
    private val ctx = application.applicationContext
    private val spHolder = SPHolder(ctx, ctx.getSharedPreferences(SP_FILE, Context.MODE_PRIVATE))

    private val responseExceptionHandler by lazy { ResponseExceptionHandler(ctx) }
    private val failureExceptionHandler by lazy { FailureExceptionHandler(ctx) }


    fun fetchWeatherForecast(postExecutor: (() -> Unit)? = null) {
        WeatherRequest(
            ctx,
            spHolder.map,
            CallbackWrapper(this, postExecutor)
        ).make()
    }

    val size get() = data.value?.list?.size ?: 0

    override fun onFailure(call: Call<DailyWeatherHolder>, t: Throwable) {
        (ctx.getSystemService(Service.CONNECTIVITY_SERVICE) as ConnectivityManager).run {
            if (activeNetwork == null || t is UnknownHostException) {
                failureExceptionHandler.handle(t)
            }
            if (call.isExecuted) failureExceptionHandler.handle(t)
        }
    }

    override fun onResponse(
        call: Call<DailyWeatherHolder>,
        response: Response<DailyWeatherHolder>
    ) {
        if (response.isSuccessful) {
            data.value = response.body()
        } else {
            responseExceptionHandler.handle(response.code())
        }
    }
}