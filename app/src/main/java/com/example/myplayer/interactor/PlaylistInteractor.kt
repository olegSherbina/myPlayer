package com.example.myplayer.interactor

import com.example.myplayer.model.Playlist
import com.example.myplayer.usecase.playlist.DeletePlaylistUseCase
import com.example.myplayer.usecase.playlist.LoadAllPlaylistsUseCase
import com.example.myplayer.usecase.playlist.LoadPlaylistUseCase
import com.example.myplayer.usecase.playlist.SavePlaylistUseCase
import javax.inject.Inject

class PlaylistInteractor @Inject constructor(
    private val loadAllPlaylistsUseCase: LoadAllPlaylistsUseCase,
    private val savePlaylistLinksUseCase: SavePlaylistUseCase,
    private val loadPlaylistLinksUseCase: LoadPlaylistUseCase,
    private val deletePlaylistLinksUseCase: DeletePlaylistUseCase
) {
    fun savePlaylist(playlist: Playlist) =
        savePlaylistLinksUseCase.invoke(
            SavePlaylistUseCase.Request(
                playlist
            )
        )

    fun loadPlaylist(playlistName: String) =
        loadPlaylistLinksUseCase.invoke(
            LoadPlaylistUseCase.Request(
                playlistName
            )
        )

    fun deletePlaylist(playlistName: String) =
        deletePlaylistLinksUseCase.invoke(
            DeletePlaylistUseCase.Request(
                playlistName
            )
        )

    fun loadAllPlaylists() =
        loadAllPlaylistsUseCase.invoke()
}
