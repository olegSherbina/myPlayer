package com.example.myplayer.ui.playeractivity.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.myplayer.R
import com.example.myplayer.player.ExoPlayerWrapper
import com.example.myplayer.service.PlayerNotificationService
import com.example.myplayer.service.PlayerNotificationService.Companion.FROM_NOTIFICATION
import com.example.myplayer.ui.mainactivity.activity.MainActivity.Companion.POSITION
import com.example.myplayer.ui.mainactivity.activity.MainActivity.Companion.VIDEOS_URL
import com.example.myplayer.ui.mainactivity.activity.MainActivity.Companion.VIDEO_THUMBNAILS_URL
import com.example.myplayer.ui.playeractivity.viewmodel.PlayerActivityViewModel
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Player.DISCONTINUITY_REASON_SEEK
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_player.*
import javax.inject.Inject

@AndroidEntryPoint
class PlayerActivity : AppCompatActivity() {

    private val viewModel: PlayerActivityViewModel by viewModels() //TODO use viewmodel for saving n stuff
    private lateinit var videoThumbnailsUrl: ArrayList<String>
    private lateinit var videosUrl: ArrayList<String>
    private var playListPosition: Int = 0
    //private var shouldSwitchToNewPosition = false

    @Inject
    lateinit var exoPlayerWrapper: ExoPlayerWrapper
    lateinit var videoPlayer: SimpleExoPlayer
    private var shouldStartService: Boolean = true
    private var uiIsHidden: Boolean = false
    private var fromNotification = false
    private val playerListener: Player.EventListener = object : Player.EventListener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            when (playbackState) {
                Player.STATE_IDLE -> {
                    val a = 69
                }
                Player.STATE_BUFFERING -> {
                    videoBufferingProgressBar.visibility = View.VISIBLE
                }
                Player.STATE_READY -> {
                    videoBufferingProgressBar.visibility = View.GONE
                }
                Player.STATE_ENDED -> {
                    val a = 69
                }
            }
        }

        override fun onPositionDiscontinuity(reason: Int) {
            super.onPositionDiscontinuity(reason)
            if (reason == DISCONTINUITY_REASON_SEEK) {
                playListPosition = videoPlayer.currentWindowIndex
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )
        setContentView(R.layout.activity_player)
        fromNotification = intent.getBooleanExtra(FROM_NOTIFICATION, false)
        videoPlayer = exoPlayerWrapper.getInstance()
        if (!fromNotification) {
            playListPosition = intent.getIntExtra(POSITION, 0)
            videoThumbnailsUrl =
                intent.getStringArrayListExtra(VIDEO_THUMBNAILS_URL) as ArrayList<String>
            videosUrl = intent.getStringArrayListExtra(VIDEOS_URL) as ArrayList<String>
            shouldStartService = savedInstanceState?.getBoolean(SHOULD_START_SERVICE) ?: true
        } else {
            shouldStartService = false
        }
        uiIsHidden = savedInstanceState?.getBoolean(UI_IS_HIDDEN) ?: false
        player_view.player = videoPlayer
        setEventListeners()
        if (shouldStartService) {
            startPlayerService()
        }
        if (uiIsHidden) {
            hideSystemUI()
            player_view.hideController()
        } else {
            showSystemUI()
            player_view.showController()
        }
    }

    private fun setEventListeners() {
        videoPlayer.addListener(playerListener)
        player_view.setControllerVisibilityListener {
            if (it == PlayerView.VISIBLE) {
                showSystemUI()
            } else {
                hideSystemUI()
            }
        }
    }

    private fun startPlayerService() {
        val serviceIntent = Intent(this, PlayerNotificationService::class.java)
        serviceIntent.putExtra(VIDEO_THUMBNAILS_URL, videoThumbnailsUrl)
        serviceIntent.putExtra(VIDEOS_URL, videosUrl)
        serviceIntent.putExtra(POSITION, playListPosition)
        startService(serviceIntent)
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
        supportActionBar?.hide()
        uiIsHidden = true
    }

    private fun showSystemUI() {
        supportActionBar?.show()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        uiIsHidden = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(SHOULD_START_SERVICE, false)
        outState.putBoolean(UI_IS_HIDDEN, uiIsHidden)
    }

    override fun onDestroy() {
        super.onDestroy()
        videoPlayer.removeListener(playerListener)
    }

    companion object {
        const val UI_IS_HIDDEN =
            "com.example.myplayer.ui.playeractivity.activity.PlayerActivity.UI_IS_HIDDEN"
        const val SHOULD_START_SERVICE =
            "com.example.myplayer.ui.playeractivity.activity.PlayerActivity.SHOULD_START_SERVICE"
        const val PLAYER_NOTIFICATION_ID = 1
    }
}
