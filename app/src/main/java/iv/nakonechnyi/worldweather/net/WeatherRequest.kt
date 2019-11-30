package iv.nakonechnyi.worldweather.net

import android.app.Service
import android.content.Context
import android.net.ConnectivityManager
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import iv.nakonechnyi.worldweather.R
import iv.nakonechnyi.worldweather.data.DailyWeatherHolder
import iv.nakonechnyi.worldweather.net.interseptors.AuthInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.Protocol
import retrofit2.*
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.UnknownHostException

class WeatherRequest(
    private val context: Context,
    private val map: Map<String, String>,
    private val callback: OnWeatherResponse,
    private val afterRun: (()-> Unit)? = null
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
        call.enqueue(object : Callback<DailyWeatherHolder> {
            override fun onFailure(call: Call<DailyWeatherHolder>, t: Throwable) {
                (context.getSystemService(Service.CONNECTIVITY_SERVICE) as ConnectivityManager).run {
                    if (activeNetwork == null || t is UnknownHostException) {
                        callback.onNoNetworkConnection(t)
                    }
                    if (call.isExecuted) callback.onRequestFailed(t)
                }

                afterRun?.invoke()

            }

            override fun onResponse(
                call: Call<DailyWeatherHolder>,
                response: Response<DailyWeatherHolder>
            ) {
                if (response.isSuccessful) callback.onResponseSuccess(response.body())
                else {
                    when (response.code()) {
                        400, 404 -> callback.onResponseFailed()
                        500 -> callback.onServerError()
                        else -> callback.onUnknownError(response.code())
                    }
                }

                afterRun?.invoke()
            }

        })
    }

}