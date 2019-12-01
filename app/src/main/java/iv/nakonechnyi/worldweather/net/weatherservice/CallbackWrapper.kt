package iv.nakonechnyi.worldweather.net.weatherservice

import iv.nakonechnyi.worldweather.data.DailyWeatherHolder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CallbackWrapper(
    private val callback: Callback<DailyWeatherHolder>,
    private val postExecutor: (() -> Unit)? = null
) : Callback<DailyWeatherHolder> {

    override fun onFailure(call: Call<DailyWeatherHolder>, t: Throwable) {
        callback.onFailure(call, t)
        postExecutor?.invoke()
    }

    override fun onResponse(
        call: Call<DailyWeatherHolder>,
        response: Response<DailyWeatherHolder>
    ) {
        callback.onResponse(call, response)
        postExecutor?.invoke()
    }
}