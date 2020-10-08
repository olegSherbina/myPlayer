package com.example.myplayer.ui.mainactivity.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.myplayer.R
import com.example.myplayer.core.utils.UrlUtils
import com.example.myplayer.service.PlayerNotificationService
import com.example.myplayer.ui.mainactivity.viewmodel.MainActivityViewModel
import com.example.myplayer.ui.playeractivity.activity.PLAYER_PLAYLIST_POSITION
import com.example.myplayer.ui.playeractivity.activity.PlayerActivity
import com.example.myplayer.ui.recyclerview.adapter.PreviewsRecyclerViewAdapter
import com.example.myplayer.ui.recyclerview.adapter.onItemClick
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val viewModel: MainActivityViewModel by viewModels()
    val videoThumbnailsUrl = ArrayList<String>()
    val videosUrl = ArrayList<String>()
    private val layoutManager: LinearLayoutManager =
        LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setPreviewsRecyclerView()
        addObservers()
        loadThumbnails()
    }

    private fun loadThumbnails() {
        viewModel.getThumbnailUrls()
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
            startActivityForResult(intent, PLAYERACTIVITY_RESULT_CODE)
            //startActivity(intent)
        }
        previewsRecyclerView.adapter?.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    private fun addObservers() {
        viewModel.thumbnailsAndVideosUrl.observe(this, Observer { thumbnailsAndVideosUrl ->
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PLAYERACTIVITY_RESULT_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val currentItem = data?.getIntExtra(PLAYER_PLAYLIST_POSITION, 0) ?: 0
                    scrollToCurrentItem(currentItem)
                }
            }
        }
    }

    private fun scrollToCurrentItem(currentItem: Int) {
        previewsRecyclerView.scrollToPosition(currentItem)
    }
}

const val VIDEOS_URL = "com.example.myplayer.ui.activity.VIDEO_URL"
const val VIDEO_THUMBNAILS_URL = "com.example.myplayer.ui.activity.VIDEO_THUMBNAIL"
const val POSITION = "com.example.myplayer.ui.activity.POSITION"
const val PLAYERACTIVITY_RESULT_CODE = 1