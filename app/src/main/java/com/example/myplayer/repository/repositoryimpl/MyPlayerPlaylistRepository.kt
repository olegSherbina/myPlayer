package com.example.myplayer.repository.repositoryimpl

import com.example.myplayer.model.MyPlayerDatabase
import com.example.myplayer.model.Playlist
import com.example.myplayer.repository.PlaylistRepository
import javax.inject.Inject

class MyPlayerPlaylistRepository @Inject constructor(
    private val database: MyPlayerDatabase
) : PlaylistRepository {

    override fun savePlaylistLinks(
        playlist: Playlist
    ) = database.playlistDao().savePlaylistLinks(playlist)

    override fun loadPlaylistLinks(playlistName: String) =
        database.playlistDao().loadPlayListLinks(playlistName)
}