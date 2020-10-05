package com.example.myplayer.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.example.myplayer.R
import com.example.myplayer.ui.recyclerview.adapter.PreviewsRecyclerViewAdapter
import com.example.myplayer.ui.recyclerview.adapter.onItemClick
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val drawable = ContextCompat.getDrawable(this, R.drawable.doge)
        val imageDrawables = arrayOf(drawable, drawable, drawable)
        previewsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        previewsRecyclerView.adapter = PreviewsRecyclerViewAdapter(imageDrawables)
        val snapHelper: SnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(previewsRecyclerView)
        previewsRecyclerView.onItemClick { recyclerView, position, v ->
            val intent = Intent(this, PlayerActivity::class.java).apply {
                putExtra(EXTRA_POSITION, position)
            }
            startActivity(intent)
        }
    }
}

const val EXTRA_POSITION = "com.example.myplayer.ui.activity.POSITION"