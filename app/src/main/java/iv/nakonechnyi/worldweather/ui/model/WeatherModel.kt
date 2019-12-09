package iv.nakonechnyi.worldweather.ui.model

import android.app.Application
import android.app.Service
import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import iv.nakonechnyi.worldweather.data.DailyWeatherHolder
import iv.nakonechnyi.worldweather.database.dao.WeatherDao
import iv.nakonechnyi.worldweather.etc.SPHolder
import iv.nakonechnyi.worldweather.etc.SP_FILE
import java.util.*

class WeatherModel(application: Application) :
    AndroidViewModel(application), Observer/*,
    Callback<DailyWeatherHolder>*/ {

    private val ctx = application.applicationContext
    private val spHolder = SPHolder(ctx, ctx.getSharedPreferences(SP_FILE, Context.MODE_PRIVATE))
    private val dailyWeatherHolder get() = weatherDao.take(spHolder.map["q"]!!.replaceAfter(",", ""))

    private val weatherDao = WeatherDao.apply {
        context = ctx
        addObserver(this@WeatherModel)
    }

    val data = MutableLiveData<DailyWeatherHolder?>().apply {
        if (!isNetworkAvailable) {
            value = dailyWeatherHolder
        }
    }

    val size get() = data.value?.list?.size ?: 0
    val mapLayer = MutableLiveData<String>()

    val isNetworkAvailable: Boolean =
        (ctx.getSystemService(Service.CONNECTIVITY_SERVICE) as ConnectivityManager)
            .run{ activeNetwork != null }

    fun refreshModel(){
        data.value = dailyWeatherHolder
    }

    override fun update(supplier: Observable, args: Any?) {
        Toast.makeText(ctx, "Update database making...", Toast.LENGTH_LONG).show()
        if(supplier.hasChanged()){
            refreshModel()
        }
    }

/*
    private val responseExceptionHandler by lazy { ResponseExceptionHandler(ctx) }
    private val failureExceptionHandler by lazy { FailureExceptionHandler(ctx) }


    fun fetchWeatherForecast(postExecutor: (() -> Unit)? = null) {
        WeatherRequest(
            ctx,
            spHolder.map,
            CallbackWrapper(this, postExecutor)
        ).make()
    }

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
    }*/

    override fun onCleared() {
        super.onCleared()
        weatherDao.closeDatabase()
    }
}