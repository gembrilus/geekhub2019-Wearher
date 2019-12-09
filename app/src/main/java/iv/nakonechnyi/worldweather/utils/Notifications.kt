package iv.nakonechnyi.worldweather.utils

import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import iv.nakonechnyi.worldweather.R
import iv.nakonechnyi.worldweather.etc.NOTIFICATION_CHANNEL_ID

private var NOTE_ID_ = 0
private val NOTE_ID = NOTE_ID_++

fun makeSimpleNotification(context: Context,
                           title: String,
                           text: String) {

    val note = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
        .setSmallIcon(R.drawable.sunny)
        .setContentTitle(title)
        .setContentText(text)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    with(NotificationManagerCompat.from(context)){
        notify(NOTE_ID, note.build())
    }
}

fun makeActionNotification(context: Context,
                              title: String,
                              intent: PendingIntent? = null){
    val note = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
        .setSmallIcon(R.drawable.sunny)
        .setContentTitle(title)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(intent)
        .setAutoCancel(true)
    with(NotificationManagerCompat.from(context)){
        notify(NOTE_ID, note.build())
    }
}