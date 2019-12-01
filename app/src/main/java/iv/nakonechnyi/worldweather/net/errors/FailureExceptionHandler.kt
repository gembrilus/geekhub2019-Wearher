package iv.nakonechnyi.worldweather.net.errors

import android.content.Context
import iv.nakonechnyi.worldweather.utils.showErrorMessage
import java.net.UnknownHostException

class FailureExceptionHandler(private val ctx: Context) : OnFailureException{
    override fun handle(t: Throwable) {
        when(t){
            is UnknownHostException -> showErrorMessage(ctx, "")
            else -> showErrorMessage(ctx, "")
        }
    }
}