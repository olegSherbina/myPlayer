package com.example.myplayer.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.myplayer.core.utils.DataConverter

@Entity
@TypeConverters(DataConverter::class)
data class Playlist(
    @PrimaryKey(autoGenerate = true) val playlistId: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "playlist") val playlist: List<Pair<String, String>>
) {
    constructor(name: String, playlist: List<Pair<String, String>>) : this(0, name, playlist)
}