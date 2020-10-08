package com.example.myplayer.interactor

import com.example.myplayer.model.Playlist
import com.example.myplayer.usecase.playlist.LoadPlaylistLinksUseCase
import com.example.myplayer.usecase.playlist.SavePlaylistLinksUseCase
import javax.inject.Inject


class PlaylistInteractor @Inject constructor(
    private val savePlaylistLinksLinksUseCase: SavePlaylistLinksUseCase,
    private val loadPlaylistLinksLinksUseCase: LoadPlaylistLinksUseCase
) {
    fun savePlaylistLinks(playlist: Playlist) =
        savePlaylistLinksLinksUseCase.invoke(
            SavePlaylistLinksUseCase.Request(
                playlist
            )
        )

    fun loadPlaylistLinks(playlistName: String) =
        loadPlaylistLinksLinksUseCase.invoke(
            LoadPlaylistLinksUseCase.Request(
                playlistName
            )
        )

}
