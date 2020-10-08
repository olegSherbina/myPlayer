package com.example.myplayer.core.base

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import com.example.myplayer.player.ExoPlayerWrapper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication: Application(){
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
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
    }

    override fun onTerminate() {
        ExoPlayerWrapper(this).getInstance().release()
        super.onTerminate()
    }

    companion object{
        const val CHANNEL_NOTIFICATION_ID =
            "com.example.myplayer.ui.playeractivity.activity.PlayerActivity.CHANNEL_NOTIFICATION_ID"
    }
}