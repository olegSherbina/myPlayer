package com.example.myplayer.repository

import com.example.myplayer.entities.Playlist
import io.reactivex.Completable
import io.reactivex.Single


interface PlaylistRepository {

    fun loadAllPlaylists(): Single<List<Playlist>>

    fun savePlaylist(playlist: Playlist): Completable

    fun loadPlaylist(playlistName: String): Single<Playlist>

    fun deletePlaylist(playlistName: String): Completable
}