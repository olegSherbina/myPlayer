package com.example.myplayer.ui.mainactivity.activity

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.myplayer.R
import com.example.myplayer.ui.mainactivity.viewmodel.MainActivityViewModel
import com.example.myplayer.ui.playeractivity.activity.PlayerActivity
import com.example.myplayer.ui.recyclerview.adapter.PreviewsRecyclerViewAdapter
import com.example.myplayer.ui.recyclerview.adapter.onItemClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainActivityViewModel by viewModels()
    var firstTimeLoad = true
    val listOfItems = mutableListOf<String>()
    private val layoutManager: LinearLayoutManager =
        LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setPreviewsRecyclerView()
    }

    private fun setPreviewsRecyclerView() {
        //TODO get urls from viewmodel
        for (i in 0 until 3) {
            listOfItems.add("https://img.youtube.com/vi/Asv9bY2jeVU/maxresdefault.jpg")
        }
        previewsRecyclerView.adapter = PreviewsRecyclerViewAdapter(listOfItems)
        previewsRecyclerView.layoutManager = layoutManager
        previewsRecyclerView.adapter?.notifyDataSetChanged()
        val snapHelper: SnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(previewsRecyclerView)
        previewsRecyclerView.onItemClick { recyclerView, position, v ->
            val intent = Intent(this, PlayerActivity::class.java).apply {
                putExtra(EXTRA_POSITION, position)
            }
            startActivity(intent)
        }
        addNewItemOnScroll()
        previewsRecyclerView.adapter?.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        firstTimeLoad = false
    }

    private fun addNewItemOnScroll() {
        previewsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollHorizontally(1)) {
                    listOfItems.add("https://img.youtube.com/vi/Asv9bY2jeVU/maxresdefault.jpg")
                    previewsRecyclerView.adapter?.notifyDataSetChanged()
                }
            }
        })
    }

    /*override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //val previewsRecyclerViewPosition = (previewsRecyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        outState.putBoolean("firstTimeLoad", firstTimeLoad)
    }*/
}

const val EXTRA_POSITION = "com.example.myplayer.ui.activity.POSITION"