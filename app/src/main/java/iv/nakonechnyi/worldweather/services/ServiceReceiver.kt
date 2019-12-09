package iv.nakonechnyi.worldweather.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import iv.nakonechnyi.worldweather.etc.SERVICE_ERROR_CODE
import iv.nakonechnyi.worldweather.etc.SERVICE_STATUS

class ServiceReceiver : BroadcastReceiver() {

    var serviceStatusListener: WeatherService.ServiceStatusListener? = null
    set(value) {
        if (serviceStatusListener == null) {
            field = value
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        val status = intent.getSerializableExtra(SERVICE_STATUS) as Status
        when(status){
            Status.START -> if (serviceStatusListener != null){
                serviceStatusListener!!.start()
            }
            Status.UPDATE -> if (serviceStatusListener != null){
                serviceStatusListener!!.update()
            }
            Status.STOP -> if (serviceStatusListener != null){
                serviceStatusListener!!.stop()
            }
            Status.ON_RESPONSE_ERROR -> if (serviceStatusListener != null){
                val code = intent.getIntExtra(SERVICE_ERROR_CODE, -1)
                serviceStatusListener!!.onServiceResponseError(code)
            }
            else -> if (serviceStatusListener != null){
                serviceStatusListener!!.onServiceError(status)
            }
        }
    }
}
