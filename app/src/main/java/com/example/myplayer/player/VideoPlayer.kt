package com.example.myplayer.player

import android.content.Context
import com.google.android.exoplayer2.SimpleExoPlayer

class VideoPlayer {
    companion object {
        private var instance: SimpleExoPlayer? = null
        fun getInstance(context: Context): SimpleExoPlayer? {
            if (instance == null) {
                instance = SimpleExoPlayer.Builder(context).build()
            }
            return instance
        }
    }
}