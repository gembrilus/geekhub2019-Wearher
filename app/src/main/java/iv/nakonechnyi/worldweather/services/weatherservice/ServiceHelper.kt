package iv.nakonechnyi.worldweather.services.weatherservice

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import iv.nakonechnyi.worldweather.MainActivity
import iv.nakonechnyi.worldweather.utils.makeActionNotification

class ServiceHelper(private val context: Context) {
    fun notifyWeatherWasChanged(){
        makeActionNotification(
            context,
            "Weather was changed!",
            PendingIntent.getActivity(
                context,
                1,
                Intent(context, MainActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        )
    }

    fun notifyWeatherWasInserted(){
        makeActionNotification(
            context,
            "New weather is coming!",
            PendingIntent.getActivity(
                context,
                1,
                Intent(context, MainActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        )
    }
}