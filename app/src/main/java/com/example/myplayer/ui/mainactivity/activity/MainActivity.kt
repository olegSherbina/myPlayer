package com.example.myplayer.ui.mainactivity.activity

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
import com.example.myplayer.ui.mainactivity.viewmodel.MainActivityViewModel
import com.example.myplayer.ui.playeractivity.activity.PlayerActivity
import com.example.myplayer.ui.recyclerview.adapter.PreviewsRecyclerViewAdapter
import com.example.myplayer.ui.recyclerview.adapter.onItemClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainActivityViewModel by viewModels()
    val listOfItems = mutableListOf<String>()
    var videoUrls = mutableListOf<String>()
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
        previewsRecyclerView.adapter = PreviewsRecyclerViewAdapter(listOfItems)
        previewsRecyclerView.layoutManager = layoutManager
        val snapHelper: SnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(previewsRecyclerView)
        previewsRecyclerView.onItemClick { recyclerView, position, v ->
            val intent = Intent(this, PlayerActivity::class.java).apply {
                putExtra(VIDEO_URL, videoUrls[position])
            }
            startActivity(intent)
        }
        previewsRecyclerView.adapter?.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    private fun addObservers() {
        viewModel.thumbnailsAndVideosUrl.observe(this, Observer { thumbnailsAndVideosUrl ->
            listOfItems.clear()
            thumbnailsAndVideosUrl.forEach { thumbnailAndVideoUrl ->
                this.videoUrls.add(thumbnailAndVideoUrl.second)
                val sb: StringBuilder = StringBuilder()
                sb.apply {
                    append(getString(R.string.thumbnail_url_template))
                    append(UrlUtils.convertToThumbnailURL(thumbnailAndVideoUrl.first))
                    append(getString(R.string.thumbnail_size))
                }
                val thumbnailURL = sb.toString()
                listOfItems.add(thumbnailURL)
            }
            previewsRecyclerView.adapter?.notifyDataSetChanged()
        })
    }
}

const val VIDEO_URL = "com.example.myplayer.ui.activity.POSITION"