package com.example.myplayer.service

import android.app.*
import android.content.Intent
import android.os.IBinder
import com.example.myplayer.R
import com.example.myplayer.core.base.CHANNEL_NOTIFICATION_ID
import com.example.myplayer.core.utils.NotificationDescriptionAdapter
import com.example.myplayer.player.ExoPlayerWrapper
import com.example.myplayer.ui.mainactivity.activity.POSITION
import com.example.myplayer.ui.mainactivity.activity.VIDEOS_URL
import com.example.myplayer.ui.mainactivity.activity.VIDEO_THUMBNAILS_URL
import com.example.myplayer.ui.playeractivity.activity.PLAYER_NOTIFICATION_ID
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_player.*
import javax.inject.Inject

@AndroidEntryPoint
class PlayerNotificationService : IntentService(PlayerNotificationService::class.java.simpleName) {
    @Inject
    lateinit var exoPlayerWrapper: ExoPlayerWrapper
    lateinit var videoPlayer: SimpleExoPlayer
    private lateinit var playerNotificationManager: PlayerNotificationManager
    private lateinit var videoThumbnailsUrl: ArrayList<String>
    private lateinit var videosUrl: ArrayList<String>
    private var playListPosition: Int = 0

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onHandleIntent(p0: Intent?) {

    }


    override fun onCreate() {
        super.onCreate()
        videoPlayer = exoPlayerWrapper.getInstance()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            videosUrl = intent.getStringArrayListExtra(VIDEOS_URL) as ArrayList<String>
            videoThumbnailsUrl =
                intent.getStringArrayListExtra(VIDEO_THUMBNAILS_URL) as ArrayList<String>
            playListPosition = intent.getIntExtra(POSITION, 0)
            addNotificationToPlayer(videoThumbnailsUrl[playListPosition])
            setPlayer()
        }
        return START_NOT_STICKY
    }

    private fun setPlayer() {
        val playlistItems: List<MediaItem> = videosUrl.map { MediaItem.fromUri(it) }
        videoPlayer.seekTo(playListPosition, 0)
        videoPlayer.setMediaItems(playlistItems)
        videoPlayer.setPlayWhenReady(true);
        videoPlayer.prepare()
    }

    private fun addNotificationToPlayer(videoThumbnailUrl: String) {
        playerNotificationManager = PlayerNotificationManager(
            this,
            CHANNEL_NOTIFICATION_ID,
            PLAYER_NOTIFICATION_ID,
            NotificationDescriptionAdapter(
                videoThumbnailUrl,
                getString(R.string.content_title_placeholder),
                getString(R.string.content_description_placeholder)
            ),
            object : PlayerNotificationManager.NotificationListener {
                override fun onNotificationPosted(
                    notificationId: Int,
                    notification: Notification,
                    ongoing: Boolean
                ) {
                    super.onNotificationPosted(notificationId, notification, ongoing)
                    if (!ongoing) {
                        stopForeground(false)
                    } else {
                        startForeground(notificationId, notification)
                    }
                }

                override fun onNotificationCancelled(
                    notificationId: Int,
                    dismissedByUser: Boolean
                ) {
                    playerNotificationManager.setPlayer(null)
                    videoPlayer.stop()
                    super.onNotificationCancelled(notificationId, dismissedByUser)
                    stopSelf()
                }

            }
        )
        playerNotificationManager.setPlayer(videoPlayer)
    }

    override fun onDestroy() {
        playerNotificationManager.setPlayer(null)
        videoPlayer.stop()
        super.onDestroy()
    }
}

const val VIDEO_THUMBNAIL_URL =
    "com.example.myplayer.service.PlayerNotificationService.VIDEO_THUMBNAIL_URL"