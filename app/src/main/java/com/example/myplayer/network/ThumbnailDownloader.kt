package com.example.myplayer.network

import android.widget.ImageView
import com.squareup.picasso.Picasso

class ThumbnailDownloader {
    companion object {
        public fun loadThumbnail(url: String, imageView: ImageView) {
            Picasso.get().isLoggingEnabled = true
            Picasso.get().load(url).into(imageView)
        }
    }

}