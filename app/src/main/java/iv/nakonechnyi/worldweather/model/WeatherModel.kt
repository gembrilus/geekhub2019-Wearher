package iv.nakonechnyi.worldweather.model

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import iv.nakonechnyi.worldweather.R
import iv.nakonechnyi.worldweather.data.*
import iv.nakonechnyi.worldweather.etc.SPHolder
import iv.nakonechnyi.worldweather.etc.SP_FILE
import iv.nakonechnyi.worldweather.net.OnWeatherResponse
import iv.nakonechnyi.worldweather.net.WeatherRequest

class WeatherModel(application: Application) :
    AndroidViewModel(application),
    OnWeatherResponse {

    val data = MutableLiveData<DailyWeatherHolder?>()
    private val ctx = application.applicationContext
    private val spHolder = SPHolder(ctx, ctx.getSharedPreferences(SP_FILE, Context.MODE_PRIVATE))

    fun fetchWeatherForecast(postExecute: (() -> Unit)? = null){
        WeatherRequest(ctx, spHolder.map, this, postExecute).make()
    }

    val size get() = data.value?.list?.size ?: 0

    override fun onNoNetworkConnection(t: Throwable) {
        Toast.makeText(ctx, ctx.getString(R.string.no_network_connection), Toast.LENGTH_LONG).show()
        t.printStackTrace()
    }

    override fun onRequestFailed(t: Throwable) {
        Toast.makeText(ctx, ctx.getString(R.string.programm_error), Toast.LENGTH_LONG).show()
        t.printStackTrace()
    }

    override fun onResponseFailed() {
        Toast.makeText(ctx, ctx.getString(R.string.no_url_or_broken), Toast.LENGTH_LONG).show()
    }

    override fun onServerError() {
        Toast.makeText(ctx, ctx.getString(R.string.server_error_500), Toast.LENGTH_LONG).show()
    }

    override fun onUnknownError(i: Int) =
        Toast.makeText(ctx, ctx.getString(R.string.unknown_error, i), Toast.LENGTH_LONG).show()

    override fun onResponseSuccess(dailyWeatherHolder: DailyWeatherHolder?) {
        data.value = dailyWeatherHolder
    }

}