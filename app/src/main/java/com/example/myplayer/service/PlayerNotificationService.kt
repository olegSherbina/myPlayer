package com.example.myplayer.service

import android.app.IntentService
import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.IBinder
import com.example.myplayer.R
import com.example.myplayer.core.base.MyApplication.Companion.CHANNEL_NOTIFICATION_ID
import com.example.myplayer.core.player.MyPlayer
import com.example.myplayer.ui.mainactivity.activity.MainActivity.Companion.POSITION
import com.example.myplayer.ui.mainactivity.activity.MainActivity.Companion.VIDEOS_URL
import com.example.myplayer.ui.mainactivity.activity.MainActivity.Companion.VIDEO_THUMBNAILS_URL
import com.example.myplayer.ui.playeractivity.activity.PlayerActivity
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.properties.Delegates

@AndroidEntryPoint
class PlayerNotificationService : IntentService(PlayerNotificationService::class.java.simpleName) {
    @Inject
    lateinit var myPlayer: MyPlayer
    lateinit var videoPlayer: SimpleExoPlayer
    private lateinit var playerNotificationManager: PlayerNotificationManager
    private lateinit var videoThumbnailsUrl: ArrayList<String>
    private lateinit var videosUrl: ArrayList<String>
    private var isOffline by Delegates.notNull<Boolean>()
    private var playListPosition: Int = 0

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onHandleIntent(p0: Intent?) {
        //NOP
    }


    override fun onCreate() {
        super.onCreate()
        videoPlayer = myPlayer.getExoPlayerInstance()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            videosUrl = intent.getStringArrayListExtra(VIDEOS_URL) as ArrayList<String>
            videoThumbnailsUrl =
                intent.getStringArrayListExtra(VIDEO_THUMBNAILS_URL) as ArrayList<String>
            playListPosition = intent.getIntExtra(POSITION, 0)
            isOffline = intent.getBooleanExtra(OFFLINE, false)
            addNotificationToPlayer()
            setPlayer()
        }
        return START_NOT_STICKY
    }

    private fun setPlayer() {
        val playlistItems: List<MediaItem> = videosUrl.map {
            generateMediaItem(it)
        }
        videoPlayer.setMediaItems(playlistItems)
        videoPlayer.seekTo(playListPosition, 0)
        videoPlayer.playWhenReady = true
        videoPlayer.prepare()
    }

    private fun generateMediaItem(url: String): MediaItem {
        return MediaItem.fromUri(url)
    }

    private fun addNotificationToPlayer() {
        playerNotificationManager = PlayerNotificationManager(
            this,
            CHANNEL_NOTIFICATION_ID,
            PLAYER_NOTIFICATION_ID,
            object : PlayerNotificationManager.MediaDescriptionAdapter {
                override fun getCurrentContentTitle(player: Player): String {
                    return getString(R.string.player_notification_title_placeholder)
                }

                override fun getCurrentContentText(player: Player): String? {
                    return getString(R.string.player_notification_description_placeholder)
                }

                override fun getCurrentLargeIcon(
                    player: Player,
                    callback: PlayerNotificationManager.BitmapCallback
                ): Bitmap? {
                    var result: Bitmap? = null
                    val target = object : Target {
                        override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                            result = bitmap
                        }

                        override fun onBitmapFailed(e: Exception, errorDrawable: Drawable?) {}
                        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
                    }
                    Picasso.get().load(videoThumbnailsUrl[videoPlayer.currentWindowIndex])
                        .into(target)
                    return result
                }

                override fun createCurrentContentIntent(player: Player): PendingIntent? {
                    val intent = Intent(this@PlayerNotificationService, PlayerActivity::class.java)
                    intent.putExtra(FROM_NOTIFICATION, true)
                    return PendingIntent.getActivity(
                        this@PlayerNotificationService,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                }
            },
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

    companion object {
        const val FROM_NOTIFICATION =
            "com.example.myplayer.service.PlayerNotificationService.FROM_NOTIFICATION"
        const val OFFLINE =
            "com.example.myplayer.service.PlayerNotificationService.OFFLINE"
        const val PLAYER_NOTIFICATION_ID = 1
    }
}

