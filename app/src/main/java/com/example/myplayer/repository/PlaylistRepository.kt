package com.example.myplayer.repository

import com.example.myplayer.model.Playlist
import io.reactivex.Completable
import io.reactivex.Single


interface PlaylistRepository {

    fun savePlaylistLinks(playlist: Playlist): Completable

    fun loadPlaylistLinks(playlistName: String): Single<Playlist>
}