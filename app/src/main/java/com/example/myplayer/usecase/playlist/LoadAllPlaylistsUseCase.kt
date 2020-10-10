package com.example.myplayer.usecase.playlist

import com.example.myplayer.model.Playlist
import com.example.myplayer.repository.PlaylistRepository
import io.reactivex.Single
import javax.inject.Inject

class LoadAllPlaylistsUseCase @Inject constructor(private val repository: PlaylistRepository) :
    Function0<Single<List<Playlist>>> {
    override fun invoke() = repository.loadAllPlaylists()
}