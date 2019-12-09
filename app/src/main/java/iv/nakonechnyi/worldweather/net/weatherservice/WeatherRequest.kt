package iv.nakonechnyi.worldweather.net.weatherservice

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import iv.nakonechnyi.worldweather.R
import iv.nakonechnyi.worldweather.data.DailyWeatherHolder
import iv.nakonechnyi.worldweather.net.interceptors.AuthInterceptor
import iv.nakonechnyi.worldweather.net.interceptors.LangInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.Protocol
import retrofit2.*
import retrofit2.converter.moshi.MoshiConverterFactory

class WeatherRequest(
    private val context: Context,
    private val map: Map<String, String>,
    private val callback: Callback<DailyWeatherHolder>
) {

    private val cacheDir by lazy {
        createTempDir(prefix = "cache", directory = context.filesDir)
    }

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .cache(Cache(cacheDir, 1000000))
        .protocols(listOf(Protocol.HTTP_1_1))
        .addInterceptor(LangInterceptor(context))
        .addInterceptor(AuthInterceptor(context))
        .addInterceptor(ChuckerInterceptor(context))
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(context.getString(R.string.baseUrlForecast))
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    private val client = retrofit.create(WeatherClient::class.java)

    fun make() {
        val call = client.getWeatherForecast(map)
        call.enqueue(callback)
    }
}