package com.example.myplayer.usecase.playlist

import com.example.myplayer.model.Playlist
import com.example.myplayer.repository.PlaylistRepository
import io.reactivex.Completable
import javax.inject.Inject

class SavePlaylistUseCase @Inject constructor(private val repository: PlaylistRepository) :
    Function1<SavePlaylistUseCase.Request, Completable> {

    override fun invoke(request: Request) = repository.savePlaylist(request.playlist)

    data class Request(val playlist: Playlist)
}