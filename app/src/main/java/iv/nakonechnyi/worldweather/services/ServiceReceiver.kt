package iv.nakonechnyi.worldweather.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import iv.nakonechnyi.worldweather.etc.SERVICE_ERROR_CODE
import iv.nakonechnyi.worldweather.etc.SERVICE_STATUS

class ServiceReceiver : BroadcastReceiver() {

    var serviceStatusListener: WeatherService.ServiceStatusListener? = null

    override fun onReceive(context: Context, intent: Intent) {

        val status = intent.getSerializableExtra(SERVICE_STATUS) as Status

        when(status){
            Status.START -> serviceStatusListener?.start()
            Status.UPDATE -> serviceStatusListener?.update()
            Status.STOP -> serviceStatusListener?.stop()
            Status.ON_RESPONSE_ERROR -> {
                val code = intent.getIntExtra(SERVICE_ERROR_CODE, -1)
                serviceStatusListener?.onServiceResponseError(code)
            }
            else -> serviceStatusListener?.onServiceError(status)
        }

    }
}
