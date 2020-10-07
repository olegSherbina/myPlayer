package com.example.myplayer.player

import android.content.Context
import androidx.activity.ComponentActivity
import com.google.android.exoplayer2.SimpleExoPlayer
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExoPlayerWrapper @Inject constructor(
    @ApplicationContext context: Context
){
    private val instance: SimpleExoPlayer = SimpleExoPlayer.Builder(context).build()
    fun getInstance(): SimpleExoPlayer {
        return instance
    }
}