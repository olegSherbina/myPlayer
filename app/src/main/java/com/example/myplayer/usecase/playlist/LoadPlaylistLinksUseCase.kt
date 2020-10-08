package com.example.myplayer.usecase.playlist

import com.example.myplayer.model.Playlist
import com.example.myplayer.repository.PlaylistRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class LoadPlaylistLinksUseCase @Inject constructor(private val repository: PlaylistRepository) :
    Function1<LoadPlaylistLinksUseCase.Request, Single<Playlist>> {

    override fun invoke(request: Request) = repository.loadPlaylistLinks(request.playlistName)

    data class Request(val playlistName: String)
}