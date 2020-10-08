package com.example.myplayer.player

import android.content.Context
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSourceFactory
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExoPlayerWrapper @Inject constructor(
    @ApplicationContext context: Context
) {
    private val instance: SimpleExoPlayer = SimpleExoPlayer.Builder(context)
        /*.setMediaSourceFactory(
            DefaultMediaSourceFactory(
                OkHttpDataSourceFactory(
                    Retrofit.Builder()
                        .baseUrl("http://baseurl.com")
                        .client(OkHttpClient())
                        //.addInterceptor(LoggingInterceptor())
                        .build().callFactory()
                )
            )
        )*/
        .build()

    fun getInstance(): SimpleExoPlayer {
        return instance
    }

}