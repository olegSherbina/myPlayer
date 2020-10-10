package com.example.myplayer.core.player

import android.content.Context
import com.example.myplayer.network.CacheDataSourceFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.tonyodev.fetch2.FetchConfiguration
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MyPlayer @Inject constructor(
    @ApplicationContext context: Context
) {
    private val instance: SimpleExoPlayer = SimpleExoPlayer.Builder(context)
        .setMediaSourceFactory(
            DefaultMediaSourceFactory(
                DefaultDataSourceFactory(context, CacheDataSourceFactory(context))
                /*OkHttpDataSourceFactory(
                    Retrofit.Builder()
                        .baseUrl("http://baseurl.com")
                        .client(OkHttpClient())
                        //.addInterceptor(LoggingInterceptor())
                        .build().callFactory()
                )*/
            )
        )
        .build()

    fun getInstance(): SimpleExoPlayer {
        return instance
    }
}