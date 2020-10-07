package com.example.myplayer.service

import android.app.*
import android.content.Intent
import android.os.IBinder
import com.example.myplayer.R
import com.example.myplayer.core.base.CHANNEL_NOTIFICATION_ID
import com.example.myplayer.core.utils.NotificationDescriptionAdapter
import com.example.myplayer.player.VideoPlayer
import com.example.myplayer.ui.playeractivity.activity.PLAYER_NOTIFICATION_ID
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerNotificationManager

class PlayerNotificationService : IntentService(PlayerNotificationService::class.java.simpleName) {

    private var videoPlayer: SimpleExoPlayer? = null
    private val NOTIFICATION_CHANNEL_ID = "playback_channel"
    private val NOTIFICATION_ID = 2
    private var playerNotificationManager: PlayerNotificationManager? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onHandleIntent(p0: Intent?) {
        
    }


    override fun onCreate() {
        super.onCreate()
        videoPlayer = VideoPlayer.getInstance(this)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val thumbnailUrl = intent.getStringExtra(VIDEO_THUMBNAIL_URL)?: ""
        addNotificationToPlayer(thumbnailUrl)
        return START_NOT_STICKY
    }

    private fun addNotificationToPlayer(videoThumbnailUrl: String) {
        if (videoPlayer != null) {

            if(playerNotificationManager == null) {
                playerNotificationManager = PlayerNotificationManager(
                    this,
                    CHANNEL_NOTIFICATION_ID,
                    PLAYER_NOTIFICATION_ID,
                    NotificationDescriptionAdapter(
                        videoThumbnailUrl,
                        getString(R.string.content_title_placeholder),
                        getString(R.string.content_description_placeholder)
                    )
                )
                playerNotificationManager?.setPlayer(videoPlayer)
                    object : PlayerNotificationManager.NotificationListener {

                        override fun onNotificationPosted(notificationId: Int,
                                                          notification: Notification,
                                                          ongoing: Boolean) {
                            super.onNotificationPosted(notificationId, notification, ongoing)
                            if (!ongoing) {
                                stopForeground(false)
                            } else {
                                startForeground(notificationId, notification)
                            }

                        }

                        override fun onNotificationCancelled(notificationId: Int,
                                                             dismissedByUser: Boolean) {
                            super.onNotificationCancelled(notificationId, dismissedByUser)
                            stopSelf()
                        }

                    }
                playerNotificationManager?.setPlayer(videoPlayer)
            }
        }

    }

    override fun onDestroy() {
        playerNotificationManager?.setPlayer(null)
        playerNotificationManager = null
        super.onDestroy()
    }
}
const val VIDEO_THUMBNAIL_URL = "com.example.myplayer.service.PlayerNotificationService.VIDEO_THUMBNAIL_URL"