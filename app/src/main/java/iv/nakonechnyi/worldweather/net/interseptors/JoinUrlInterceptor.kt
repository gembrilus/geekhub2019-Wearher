package iv.nakonechnyi.worldweather.net.interseptors

import okhttp3.Interceptor
import okhttp3.Response

class JoinUrlInterceptor(private val options: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain.request().newBuilder().url(
                chain.request().url.newBuilder()
                    .addPathSegment(options)
                    .build()
            ).build()
        )
    }
}