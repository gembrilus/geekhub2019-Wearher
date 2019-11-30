package iv.nakonechnyi.worldweather.net

import iv.nakonechnyi.worldweather.data.DailyWeatherHolder

interface OnWeatherResponse{
    fun onNoNetworkConnection(t: Throwable)
    fun onRequestFailed(t: Throwable)
    fun onResponseFailed()
    fun onServerError()
    fun onUnknownError(i: Int)
    fun onResponseSuccess(dailyWeatherHolder: DailyWeatherHolder?)
}