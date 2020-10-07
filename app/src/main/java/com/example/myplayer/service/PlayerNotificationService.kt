package com.example.myplayer.service

import android.app.*
import android.content.Intent
import android.os.Build
import androidx.core.app.JobIntentService
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.myplayer.R

class PlayerNotificationService : IntentService(PlayerNotificationService::class.java.simpleName) {
    
    override fun onCreate() {
        super.onCreate()
    }

    override fun onHandleIntent(p0: Intent?) {
        val a = 69
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val pendingIntent: PendingIntent =
            Intent(this, PlayerNotificationService::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }

        val notificationManager =
            NotificationManagerCompat.from(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_NOTIFICATION_ID,
                    "My Player channel",
                    NotificationManager.IMPORTANCE_LOW
                )
            )
        }
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_NOTIFICATION_ID)
            .setContentTitle(getText(R.string.player_notification_title))
            .setContentText(getText(R.string.player_notification_message))
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            //.setTicker(getText(R.string.ticker_text))
            .build()

        // Notification ID cannot be 0.
        startForeground(PLAYER_NOTIFICATION_ID, notification)
        return Service.START_STICKY
    }
}

const val PLAYER_NOTIFICATION_ID = 1337
const val CHANNEL_NOTIFICATION_ID =
    "com.example.myplayer.service.PlayerNotificationService.CHANNEL_NOTIFICATION_ID"