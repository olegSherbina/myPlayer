package com.example.myplayer.core.player

import android.content.Context
import com.example.myplayer.network.MyCacheDataSourceFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MyPlayer @Inject constructor(
    @ApplicationContext context: Context
) {
    private val instance: SimpleExoPlayer by lazy {SimpleExoPlayer.Builder(context)
        .setMediaSourceFactory(
            DefaultMediaSourceFactory(
                DefaultDataSourceFactory(context, MyCacheDataSourceFactory(context))
            )
        )
        .build()}

    fun getExoPlayerInstance(): SimpleExoPlayer {
        return instance
    }
}