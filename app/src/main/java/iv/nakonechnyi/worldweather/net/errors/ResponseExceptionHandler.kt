package iv.nakonechnyi.worldweather.net.errors

import android.content.Context
import iv.nakonechnyi.worldweather.utils.showErrorMessage

class ResponseExceptionHandler(private val ctx: Context): OnResponseException {
    override fun handle(errorCode: Int) {
        when(errorCode){
            400 -> showErrorMessage(ctx, "")
            404 -> showErrorMessage(ctx, "")
            500 -> showErrorMessage(ctx, "")
            else -> showErrorMessage(ctx, "")
        }
    }

}