package com.singlelab.lume.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.singlelab.lume.LumeApplication
import com.singlelab.lume.R
import com.singlelab.lume.model.Const
import java.util.*


class FCMService : FirebaseMessagingService() {

    companion object {
        const val CHANNEL_NAME = "Lume"
        const val CHANNEL_ID = "1"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage.notification != null) {
            val title = remoteMessage.notification!!.title
            val message = remoteMessage.notification!!.body
            var url: String? = null
            if (!remoteMessage.data.isNullOrEmpty() && remoteMessage.data.containsKey(Const.URL_KEY)) {
                url = remoteMessage.data[Const.URL_KEY]
            }
            if (!title.isNullOrEmpty() && !message.isNullOrEmpty()) {
                sendNotification(title, message, url)
            }
        }
    }

    override fun onNewToken(token: String) {
        LumeApplication.preferences?.setPushToken(token)
    }

    private fun sendNotification(title: String, message: String, url: String?) {
        val pendingIntent: PendingIntent? = if (url != null) {
            val notificationIntent = Intent(Intent.ACTION_VIEW)
            notificationIntent.data = Uri.parse(url)
            PendingIntent.getActivity(this, 0, notificationIntent, 0)
        } else {
            null
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1)
            channel.importance = NotificationManager.IMPORTANCE_HIGH
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder =
            NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                .setContentTitle(title)
                .setDefaults(Notification.DEFAULT_ALL)
                .setVibrate(longArrayOf(1))
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setChannelId(CHANNEL_ID)

        notificationManager.notify(
            Random().nextInt(Int.MAX_VALUE - 1),
            notificationBuilder.build()
        )
    }
}