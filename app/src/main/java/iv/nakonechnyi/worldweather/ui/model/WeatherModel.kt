package iv.nakonechnyi.worldweather.ui.model

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import iv.nakonechnyi.worldweather.data.DailyWeatherHolder
import iv.nakonechnyi.worldweather.database.dao.WeatherDao
import iv.nakonechnyi.worldweather.etc.SPHolder
import iv.nakonechnyi.worldweather.etc.SP_FILE

class WeatherModel(application: Application) :
    AndroidViewModel(application) {

    private val ctx by lazy { application.applicationContext }

    private val spHolder = SPHolder(
        ctx,
        ctx.getSharedPreferences(SP_FILE, Context.MODE_PRIVATE)
    )

    private val weatherDao by lazy { WeatherDao(ctx) }

    private val dailyWeatherHolder: DailyWeatherHolder?
        get() {
            val city: String = spHolder.keywords.replace(",.+".toRegex(), "")
            return weatherDao.take(city)
        }

    val data = MutableLiveData<DailyWeatherHolder?>().apply {
        value = dailyWeatherHolder
    }

    val size get() = data.value?.list?.size ?: 0

    val mapLayer = MutableLiveData<String>()


    private val _isNoNetworkConnection = MutableLiveData<Boolean>(false)
    val isNoNetworkConnection: LiveData<Boolean> get() = _isNoNetworkConnection

    fun noNetworkConnection(value: Boolean) = _isNoNetworkConnection.postValue(value)
    fun updateAppBarSubTitle(){
        noNetworkConnection(isNoNetworkConnection.value ?: false)
    }

    fun refreshModel() {
        data.value = dailyWeatherHolder
    }

    override fun onCleared() {
        super.onCleared()
        weatherDao.closeDatabase()
    }
}