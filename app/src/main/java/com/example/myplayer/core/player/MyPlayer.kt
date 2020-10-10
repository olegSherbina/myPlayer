package com.example.myplayer.core.player

import android.content.Context
import com.google.android.exoplayer2.SimpleExoPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyPlayer @Inject constructor(
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