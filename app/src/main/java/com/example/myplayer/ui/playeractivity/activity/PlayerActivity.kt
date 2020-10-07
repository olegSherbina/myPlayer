package com.example.myplayer.ui.playeractivity.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.myplayer.R
import com.example.myplayer.core.base.CHANNEL_NOTIFICATION_ID
import com.example.myplayer.core.utils.NotificationDescriptionAdapter
import com.example.myplayer.service.PlayerNotificationService
import com.example.myplayer.service.VIDEO_THUMBNAIL_URL
import com.example.myplayer.ui.mainactivity.activity.POSITION
import com.example.myplayer.ui.mainactivity.activity.VIDEOS_URL
import com.example.myplayer.ui.mainactivity.activity.VIDEO_THUMBNAILS_URL
import com.example.myplayer.ui.playeractivity.viewmodel.PlayerActivityViewModel
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_player.*


class PlayerActivity : AppCompatActivity() {

    private val viewModel: PlayerActivityViewModel by viewModels() //TODO use viewmodel for saving n stuff
    private lateinit var videoUrl: String
    private lateinit var videoThumbnailUrl: String
    private lateinit var videoThumbnailsUrl: ArrayList<String>
    private lateinit var videosUrl: ArrayList<String>
    private var playListPosition = 0
    private lateinit var videoPlayer: SimpleExoPlayer
    private var playWhenReady = true
    private var playbackPosition: Long = 0
    private lateinit var playerNotificationManager: PlayerNotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )
        setContentView(R.layout.activity_player)
        playListPosition = intent.getIntExtra(POSITION, 0)
        videoThumbnailsUrl = intent.getStringArrayListExtra(VIDEO_THUMBNAILS_URL) as ArrayList<String>
        videosUrl = intent.getStringArrayListExtra(VIDEOS_URL) as ArrayList<String>
        videoThumbnailUrl = videoThumbnailsUrl[playListPosition]
        videoUrl = videosUrl[playListPosition]
    }

    //TODO next and previous clip buttons
    private fun setPlayer() {
        videoPlayer = SimpleExoPlayer.Builder(this).build()
        player_view.player = videoPlayer
        videoPlayer.setPlayWhenReady(playWhenReady)
        videoPlayer.seekTo(this.playListPosition, playbackPosition)

        val playlistItems: List<MediaItem> = videosUrl.map { MediaItem.fromUri(it) }

        videoPlayer.setMediaItems(playlistItems)
        videoPlayer.prepare()
        setEventListeners()
        setPlayerNotification()
    }

    private fun setEventListeners() {
        videoPlayer.addListener(object : Player.DefaultEventListener() {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when (playbackState) {
                    Player.STATE_IDLE -> {

                    }
                    Player.STATE_BUFFERING -> {
                        videoBufferingProgressBar.visibility = View.VISIBLE
                    }
                    Player.STATE_READY -> {
                        videoBufferingProgressBar.visibility = View.GONE
                    }
                    Player.STATE_ENDED -> {

                    }
                }
            }
        })
        player_view.setControllerVisibilityListener {
            if (it == PlayerView.VISIBLE) {
                showSystemUI()
            } else {
                hideSystemUI()
            }
        }
    }

    private fun setPlayerNotification() {
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
        playerNotificationManager.setPlayer(videoPlayer)
        /*val serviceIntent = Intent(this, PlayerNotificationService::class.java)
        serviceIntent.putExtra(VIDEO_THUMBNAIL_URL, videoThumbnailUrl)
        startService(serviceIntent)*/
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            setPlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        if ((Util.SDK_INT < 24)) {
            setPlayer()
        }
        videoPlayer.playWhenReady = true
    }

    override fun onPause() {
        super.onPause();
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        videoPlayer.playWhenReady = false
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
        /*if (isFinishing){
            val intent = Intent(this, PlayerNotificationService::class.java)
            stopService(intent)
        }*/
    }

    private fun releasePlayer() {
        playWhenReady = videoPlayer.playWhenReady
        playbackPosition = videoPlayer.currentPosition
        playListPosition = videoPlayer.currentWindowIndex
        playerNotificationManager.setPlayer(null)
        videoPlayer.release()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(PLAYER_POSITION, videoPlayer.currentPosition)
        outState.putBoolean(KEY_PLAYER_PLAY_WHEN_READY, videoPlayer.playWhenReady)
        outState.putInt(PLAYER_PLAYLIST_POSITION, videoPlayer.currentWindowIndex)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.let {
            videoPlayer.seekTo(
                it.getInt(PLAYER_PLAYLIST_POSITION),
                it.getLong(PLAYER_POSITION)
            )
            videoPlayer.playWhenReady = it.getBoolean(KEY_PLAYER_PLAY_WHEN_READY)
            //TODO seamless transition, current one is not the best solution, should handle orientation change manually
        }
    }

    override fun onBackPressed() {
        val resultIntent = Intent()
        resultIntent.putExtra(PLAYER_PLAYLIST_POSITION, videoPlayer.currentWindowIndex)
        setResult(Activity.RESULT_OK, resultIntent)
        super.onBackPressed()
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    private fun showSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }
}

const val PLAYER_POSITION =
    "com.example.myplayer.ui.playeractivity.activity.PlayerActivity.PLAYER_POSITION"
const val PLAYER_PLAYLIST_POSITION =
    "com.example.myplayer.ui.playeractivity.activity.PlayerActivity.PLAYER_PLAYLIST_POSITION"
const val KEY_PLAYER_PLAY_WHEN_READY =
    "com.example.myplayer.ui.playeractivity.activity.PlayerActivity.PLAY_WHEN_READY"
const val PLAYER_NOTIFICATION_ID = 1
