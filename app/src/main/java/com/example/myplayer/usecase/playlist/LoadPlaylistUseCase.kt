package com.example.myplayer.usecase.playlist

import com.example.myplayer.entities.Playlist
import com.example.myplayer.repository.PlaylistRepository
import io.reactivex.Single
import javax.inject.Inject

class LoadPlaylistUseCase @Inject constructor(private val repository: PlaylistRepository) :
    Function1<LoadPlaylistUseCase.Request, Single<Playlist>> {

    override fun invoke(request: Request) = repository.loadPlaylist(request.playlistName)

    data class Request(val playlistName: String)
}