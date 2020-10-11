package com.example.myplayer.ui.playeractivity.activity

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.myplayer.R
import com.example.myplayer.core.player.MyPlayer
import com.example.myplayer.service.PlayerNotificationService
import com.example.myplayer.service.PlayerNotificationService.Companion.FROM_NOTIFICATION
import com.example.myplayer.service.PlayerNotificationService.Companion.OFFLINE
import com.example.myplayer.service.VideoDownloadService
import com.example.myplayer.service.VideoDownloadService.Companion.DOWNLOAD_URL
import com.example.myplayer.ui.mainactivity.activity.MainActivity
import com.example.myplayer.ui.mainactivity.activity.MainActivity.Companion.POSITION
import com.example.myplayer.ui.mainactivity.activity.MainActivity.Companion.VIDEOS_URL
import com.example.myplayer.ui.mainactivity.activity.MainActivity.Companion.VIDEO_THUMBNAILS_URL
import com.example.myplayer.viewmodel.PlayerActivityViewModel
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.gun0912.tedpermission.TedPermissionResult
import com.tedpark.tedpermission.rx2.TedRx2Permission
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.observers.DisposableSingleObserver
import kotlinx.android.synthetic.main.activity_player.*
import java.io.File
import javax.inject.Inject


@AndroidEntryPoint
class PlayerActivity : AppCompatActivity() {

    private val viewModel: PlayerActivityViewModel by viewModels() //TODO use viewmodel for downloading videos

    @Inject
    lateinit var myPlayer: MyPlayer
    private lateinit var videoPlayer: SimpleExoPlayer
    private lateinit var videoThumbnailsUrl: ArrayList<String>
    private lateinit var videosUrl: ArrayList<String>
    private val localVideosPaths = ArrayList<String>()
    private lateinit var mediaItems: List<MediaItem>
    private lateinit var offlineMediaItems: List<MediaItem>
    private var playListPosition: Int = 0
    private var lastWindowIndex = 0
    private var shouldStartService: Boolean = true
    private var uiIsHidden: Boolean = false
    private var fromNotification = false
    private val playerListener = MyEventListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )
        setContentView(R.layout.activity_player)
        fromNotification = intent.getBooleanExtra(FROM_NOTIFICATION, false)
        videoPlayer = myPlayer.getExoPlayerInstance()
        if (!fromNotification) {
            playListPosition = intent.getIntExtra(POSITION, 0)
            videoThumbnailsUrl =
                intent.getStringArrayListExtra(VIDEO_THUMBNAILS_URL) as ArrayList<String>
            videosUrl = intent.getStringArrayListExtra(VIDEOS_URL) as ArrayList<String>
            val localFilesDir =
                File(Environment.getExternalStorageDirectory(), "/MyPlayerVideos/downloads")
            localFilesDir.mkdirs()
            for (i in 0 until videosUrl.size) {
                localVideosPaths.add(localFilesDir.path + i.toString() + ".mp4")
            }
            mediaItems = videosUrl.map {
                generateMediaItem(it)
            }
            offlineMediaItems = localVideosPaths.map {
                generateMediaItem(it)
            }
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
        playerListener.onPositionDiscontinuity(Player.DISCONTINUITY_REASON_INTERNAL)
    }

    private fun generateMediaItem(url: String): MediaItem {
        return MediaItem.fromUri(url)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_player, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if (id == R.id.download_button) {
            TedRx2Permission.with(this)
                .setRationaleTitle(R.string.access_storage_rationale_title)
                .setRationaleMessage(R.string.access_storage_rationale_message)
                .setRationaleConfirmText(R.string.ok)
                .setDeniedCloseButtonText(R.string.no)
                .setPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .request()
                .subscribe(object : DisposableSingleObserver<TedPermissionResult>() {
                    override fun onSuccess(tedPermissionResult: TedPermissionResult) {
                        if (tedPermissionResult.isGranted) {
                            val dir = File(
                                Environment.getExternalStorageDirectory(),
                                "/MyPlayerVideos/downloads"
                            )
                            dir.mkdirs()
                            val file = File(dir, playListPosition.toString() + ".mp4")
                            if (!isServiceRunning(VideoDownloadService::class.java) && !file.exists()) {
                                startDownloadService()
                            }
                        } else {
                            Toast.makeText(
                                this@PlayerActivity,
                                getString(R.string.cannot_download),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }

                    override fun onError(throwable: Throwable) {
                        Log.e(TAG, throwable.stackTrace.toString())
                    }
                })
        }
        return super.onOptionsItemSelected(item)
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

    private fun startDownloadService() {
        val serviceIntent = Intent(this, VideoDownloadService::class.java)
        serviceIntent.putExtra(DOWNLOAD_URL, videosUrl[playListPosition])
        serviceIntent.putExtra(POSITION, playListPosition)
        startService(serviceIntent)
    }

    private fun startPlayerService() {
        val serviceIntent = Intent(this, PlayerNotificationService::class.java)
        serviceIntent.putExtra(VIDEO_THUMBNAILS_URL, videoThumbnailsUrl)
        serviceIntent.putExtra(VIDEOS_URL, videosUrl)
        serviceIntent.putExtra(POSITION, playListPosition)
        if (!isInternetConnected()) {
            serviceIntent.putExtra(OFFLINE, true)
        }
        startService(serviceIntent)
    }

    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val activityManager: ActivityManager =
            getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in activityManager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.getClassName()) {
                return true
            }
        }
        return false
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

    private fun isInternetConnected(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        if (!isConnected) {
            Log.v(MainActivity.TAG, "no Internet connection")
            Toast.makeText(
                this,
                getString(R.string.no_internet_connection_warning),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Log.v(MainActivity.TAG, "Internet is connected")
        }
        return isConnected
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

    private inner class MyEventListener : Player.EventListener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            when (playbackState) {
                Player.STATE_IDLE -> {
                    //NOP
                }
                Player.STATE_BUFFERING -> {
                    videoBufferingProgressBar.visibility = View.VISIBLE
                }
                Player.STATE_READY -> {
                    videoBufferingProgressBar.visibility = View.GONE
                }
                Player.STATE_ENDED -> {
                    //NOP
                }
            }
        }

        override fun onPositionDiscontinuity(reason: Int) {
            super.onPositionDiscontinuity(reason)
            playListPosition = videoPlayer.currentWindowIndex
            val dir = File(Environment.getExternalStorageDirectory(), "/MyPlayerVideos/downloads")
            dir.mkdirs()
            val file = File(dir, playListPosition.toString() + ".mp4")
            val latestWindowIndex: Int = videoPlayer.getCurrentWindowIndex()
            if (latestWindowIndex != lastWindowIndex) {
                lastWindowIndex = latestWindowIndex
                if (file.exists() && !isInternetConnected()) {
                    videoPlayer.setMediaItems(offlineMediaItems)
                } else {
                    videoPlayer.setMediaItems(mediaItems)
                }
            }
        }
    }

    companion object {
        const val UI_IS_HIDDEN =
            "com.example.myplayer.ui.playeractivity.activity.PlayerActivity.UI_IS_HIDDEN"
        const val SHOULD_START_SERVICE =
            "com.example.myplayer.ui.playeractivity.activity.PlayerActivity.SHOULD_START_SERVICE"
        const val TAG = "PlayerActivity"
    }
}
