package iv.nakonechnyi.worldweather.net.mapservice

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import iv.nakonechnyi.worldweather.R
import iv.nakonechnyi.worldweather.net.interseptors.AuthInterceptor
import iv.nakonechnyi.worldweather.net.interseptors.JoinUrlInterceptor
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import java.util.concurrent.TimeUnit

class WeatherMapRequest(
    ctx: Context,
    private val callback: Callback,
    options: String
) {

    private val request = Request.Builder()
        .url(ctx.getString(R.string.baseUrlForecastMap))
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .protocols(listOf(Protocol.HTTP_1_1))
        .addInterceptor(JoinUrlInterceptor(options))
        .addInterceptor(AuthInterceptor(ctx))
        .addInterceptor(ChuckerInterceptor(ctx))
        .build()

    fun make(){
        okHttpClient.newCall(request).enqueue(callback)
    }
}