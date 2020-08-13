package com.singlelab.lume.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.view.View
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.singlelab.lume.LumeApplication
import com.singlelab.lume.R
import java.util.*


class FCMService : FirebaseMessagingService() {

    companion object {
        const val APP_NAME = "Lume"
        const val CHANNEL_ID = "1567"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage.notification != null) {
            val title = remoteMessage.notification!!.title
            val message = remoteMessage.notification!!.body

            if (!title.isNullOrEmpty() && !message.isNullOrEmpty()) {
                sendNotification(title, message)
            }
        }
    }

    override fun onNewToken(token: String) {
        LumeApplication.preferences?.setPushToken(token)
    }

    private fun sendNotification(title: String, message: String) {
        val pendingIntent: PendingIntent? = null

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Lume channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.enableVibration(false)
            channel.importance = NotificationManager.IMPORTANCE_HIGH
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder =
            NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                .setContentTitle(title)
                .setDefaults(Notification.DEFAULT_ALL)
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