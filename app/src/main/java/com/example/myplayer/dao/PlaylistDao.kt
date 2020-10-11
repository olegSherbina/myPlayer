package com.example.myplayer.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myplayer.entities.Playlist
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface PlaylistDao {
    @Query("SELECT * FROM playlist")
    fun getAll(): Single<List<Playlist>>

    @Query("SELECT * FROM playlist WHERE name LIKE :name LIMIT 1")
    fun loadPlayList(name: String): Single<Playlist>

    @Insert
    fun savePlaylist(vararg playlist: Playlist): Completable

    @Query("DELETE FROM playlist WHERE name = :name")
    fun deletePlaylist(name: String): Completable
}