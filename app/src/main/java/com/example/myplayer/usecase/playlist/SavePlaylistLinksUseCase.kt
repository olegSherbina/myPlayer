package com.example.myplayer.usecase.playlist

import com.example.myplayer.model.Playlist
import com.example.myplayer.repository.PlaylistRepository
import io.reactivex.Completable
import javax.inject.Inject

class SavePlaylistLinksUseCase @Inject constructor(private val repository: PlaylistRepository) :
    Function1<SavePlaylistLinksUseCase.Request, Completable> {

    override fun invoke(request: Request) = repository.savePlaylistLinks(request.playlist)

    data class Request(val playlist: Playlist)
}