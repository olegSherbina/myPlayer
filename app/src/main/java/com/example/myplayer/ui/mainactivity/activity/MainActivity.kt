package com.example.myplayer.ui.mainactivity.activity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.myplayer.R
import com.example.myplayer.core.utils.UrlUtils
import com.example.myplayer.player.ExoPlayerWrapper
import com.example.myplayer.viewmodel.MainActivityViewModel
import com.example.myplayer.ui.playeractivity.activity.PlayerActivity
import com.example.myplayer.ui.recyclerview.adapter.PreviewsRecyclerViewAdapter
import com.example.myplayer.ui.recyclerview.adapter.onItemClick
import com.google.android.exoplayer2.SimpleExoPlayer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainActivityViewModel by viewModels()
    private val videoThumbnailsUrl = ArrayList<String>()
    private val videosUrl = ArrayList<String>()
    private var shouldScrollToPlayerPosition = true

    @Inject
    lateinit var exoPlayerWrapper: ExoPlayerWrapper
    lateinit var videoPlayer: SimpleExoPlayer
    private val layoutManager: LinearLayoutManager =
        LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkInternetConnection()
        setPreviewsRecyclerView()
        addObservers()
        loadPlaylistLinks()
        videoPlayer = exoPlayerWrapper.getInstance()
    }

    private fun checkInternetConnection() {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        if (!isConnected) {
            Log.v(TAG, "no Internet connection")
            Toast.makeText(
                this,
                getString(R.string.no_internet_connection_warning),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Log.v(TAG, "Internet is connected")
        }
    }

    private fun loadPlaylistLinks() {
        viewModel.writeTestData()
        viewModel.loadPlaylistLinks("Test playlist")
    }

    private fun setPreviewsRecyclerView() {
        previewsRecyclerView.adapter = PreviewsRecyclerViewAdapter(videoThumbnailsUrl)
        previewsRecyclerView.layoutManager = layoutManager
        val snapHelper: SnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(previewsRecyclerView)
        previewsRecyclerView.onItemClick { recyclerView, position, v ->
            val intent = Intent(this, PlayerActivity::class.java).apply {
                putExtra(POSITION, position)
                putStringArrayListExtra(VIDEO_THUMBNAILS_URL, videoThumbnailsUrl)
                putStringArrayListExtra(VIDEOS_URL, videosUrl)
            }
            startActivity(intent)
        }
        previewsRecyclerView.adapter?.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    private fun addObservers() {
        viewModel.playlistLinks.observe(this, Observer { thumbnailsAndVideosUrl ->
            videoThumbnailsUrl.clear()
            thumbnailsAndVideosUrl.forEach { thumbnailAndVideoUrl ->
                val sb: StringBuilder = StringBuilder()
                sb.apply {
                    append(getString(R.string.thumbnail_url_template))
                    append(UrlUtils.convertToThumbnailURL(thumbnailAndVideoUrl.first))
                    append(getString(R.string.thumbnail_size))
                }
                val thumbnailURL = sb.toString()
                videoThumbnailsUrl.add(thumbnailURL)
                videosUrl.add(thumbnailAndVideoUrl.second)
            }
            previewsRecyclerView.adapter?.notifyDataSetChanged()
        })
    }


    override fun onResume() {
        super.onResume()
        if (shouldScrollToPlayerPosition) {
            scrollToCurrentItem(videoPlayer.currentWindowIndex)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(POSITION, layoutManager.findFirstVisibleItemPosition())
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        scrollToCurrentItem(savedInstanceState.getInt(POSITION))
        shouldScrollToPlayerPosition = false
        super.onRestoreInstanceState(savedInstanceState)
    }

    private fun scrollToCurrentItem(currentItem: Int) {
        previewsRecyclerView.scrollToPosition(currentItem)
    }

    companion object {
        const val VIDEOS_URL = "com.example.myplayer.ui.activity.VIDEO_URL"
        const val VIDEO_THUMBNAILS_URL = "com.example.myplayer.ui.activity.VIDEO_THUMBNAIL"
        const val POSITION = "com.example.myplayer.ui.activity.POSITION"
        const val TAG = "MainActivity"
    }
}
