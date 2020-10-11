package com.example.myplayer.model

import androidx.room.*
import com.example.myplayer.dao.PlaylistDao
import com.example.myplayer.entities.Playlist

@Database(entities = arrayOf(Playlist::class), version = 1)
abstract class MyPlayerDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao
}
