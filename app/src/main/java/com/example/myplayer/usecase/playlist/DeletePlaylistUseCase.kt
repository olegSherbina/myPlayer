package com.example.myplayer.usecase.playlist

import com.example.myplayer.repository.PlaylistRepository
import io.reactivex.Completable
import javax.inject.Inject

class DeletePlaylistUseCase @Inject constructor(private val repository: PlaylistRepository) :
    Function1<DeletePlaylistUseCase.Request, Completable> {

    override fun invoke(request: Request) = repository.deletePlaylist(request.playlistName)

    data class Request(val playlistName: String)
}