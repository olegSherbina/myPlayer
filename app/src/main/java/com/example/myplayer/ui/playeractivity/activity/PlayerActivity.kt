package com.example.myplayer.ui.playeractivity.activity

import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.myplayer.R
import com.example.myplayer.ui.mainactivity.activity.VIDEO_URL
import com.example.myplayer.ui.playeractivity.viewmodel.PlayerActivityViewModel
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_player.*


class PlayerActivity : AppCompatActivity() {

    private val viewModel: PlayerActivityViewModel by viewModels() //TODO use viewmodel for saving n stuff
    private var uiIsHidden = true
    private lateinit var videoUrl: String
    private lateinit var videoPlayer: SimpleExoPlayer
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val savedUiState = savedInstanceState?.getBoolean("uiIsHidden")
        if (savedUiState != null) {
            uiIsHidden = savedUiState
        }*/
        setContentView(R.layout.activity_player)
        //setUiState()
        videoUrl = intent.getStringExtra(VIDEO_URL).toString()
        //extractYoutubeUrl()
    }

    private fun setPlayer() {
        videoPlayer = SimpleExoPlayer.Builder(this).build()
        player_view.player = videoPlayer //TODO don't forget horizontal layout
        videoPlayer.setPlayWhenReady(playWhenReady);
        videoPlayer.seekTo(currentWindow, playbackPosition);
        buildMediaSource()?.let {
            videoPlayer.setMediaSource(it)
            videoPlayer.prepare()
        }
    }

    private fun buildMediaSource(): MediaSource? {
        val dataSourceFactory = DefaultDataSourceFactory(this, "sample")
        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(Uri.parse(videoUrl)))
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
    }

    private fun releasePlayer() {
        playWhenReady = videoPlayer.playWhenReady
        playbackPosition = videoPlayer.currentPosition
        currentWindow = videoPlayer.currentWindowIndex
        videoPlayer.release()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(KEY_PLAYER_POSITION, videoPlayer.currentPosition)
        outState.putBoolean(KEY_PLAYER_PLAY_WHEN_READY, videoPlayer.playWhenReady)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.let {
            videoPlayer.seekTo(it.getLong(KEY_PLAYER_POSITION))
            videoPlayer.playWhenReady = it.getBoolean(KEY_PLAYER_PLAY_WHEN_READY)
            //TODO seamless transition, current one is not the best solution, should handle orientation change manually
        }
    }


    /*private fun setUiState() {
        if (uiIsHidden) {
            hideSystemUI()
        } else {
            showSystemUI()
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("uiIsHidden", uiIsHidden)
        super.onSaveInstanceState(outState)
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
        uiIsHidden = true
    }

    private fun showSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        uiIsHidden = false
    }*/
}

const val KEY_PLAYER_POSITION = "com.example.myplayer.ui.playeractivity.activity.PLAYER_POSITION"
const val KEY_PLAYER_PLAY_WHEN_READY =
    "com.example.myplayer.ui.playeractivity.activity.PLAY_WHEN_READY"