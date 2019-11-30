package iv.nakonechnyi.worldweather.net.interseptors

import android.content.Context
import iv.nakonechnyi.worldweather.R
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain.request().newBuilder().url(
                chain.request().url.newBuilder()
                    .addQueryParameter("APPID", context.getString(R.string.APPID))
                    .build()
            ).build()
        )
    }
}