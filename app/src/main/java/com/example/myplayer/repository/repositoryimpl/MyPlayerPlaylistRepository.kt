package com.example.myplayer.repository.repositoryimpl

import com.example.myplayer.model.MyPlayerDatabase
import com.example.myplayer.entities.Playlist
import com.example.myplayer.repository.PlaylistRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class MyPlayerPlaylistRepository @Inject constructor(
    private val database: MyPlayerDatabase
) : PlaylistRepository {
    override fun loadAllPlaylists(): Single<List<Playlist>> = database.playlistDao().getAll()

    override fun savePlaylist(
        playlist: Playlist
    ) = database.playlistDao().savePlaylist(playlist)

    override fun loadPlaylist(playlistName: String) =
        database.playlistDao().loadPlayList(playlistName)

    override fun deletePlaylist(playlistName: String): Completable = database.playlistDao().deletePlaylist(playlistName)


}