package iv.nakonechnyi.worldweather.net.errors

import android.content.Context
import iv.nakonechnyi.worldweather.R
import iv.nakonechnyi.worldweather.utils.showErrorMessage

class ResponseExceptionHandler(private val ctx: Context): OnResponseException {
    override fun handle(errorCode: Int) {
        when(errorCode){
            400 -> showErrorMessage(ctx, ctx.getString(R.string.programm_error))
            404 -> showErrorMessage(ctx, "")
            500 -> showErrorMessage(ctx, ctx.getString(R.string.server_error_500))
            else -> showErrorMessage(ctx, "")
        }
    }

}