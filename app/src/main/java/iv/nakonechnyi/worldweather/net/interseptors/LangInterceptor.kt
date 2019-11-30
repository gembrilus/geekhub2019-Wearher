package iv.nakonechnyi.worldweather.net.interseptors

import android.content.Context
import iv.nakonechnyi.worldweather.R
import okhttp3.Interceptor
import okhttp3.Response
import java.util.*

class LangInterceptor(private val context: Context) : Interceptor {

    private val lang = Locale.getDefault().country.toLowerCase(Locale.ENGLISH)
    private val langList = context.resources.getStringArray(R.array.langs)

    override fun intercept(chain: Interceptor.Chain): Response {
        if (langList.contains(lang)) {
            return chain.proceed(
                chain.request().newBuilder().url(
                    chain.request().url.newBuilder().addQueryParameter(context.getString(R.string.lang), lang).build()
                ).build()
            )
        } else {
            return chain.proceed(chain.request())
        }
    }
}