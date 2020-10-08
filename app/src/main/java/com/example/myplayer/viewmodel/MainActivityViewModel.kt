package com.example.myplayer.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.hilt.lifecycle.ViewModelInject
import com.example.myplayer.core.base.BaseViewModel
import com.example.myplayer.interactor.PlaylistInteractor
import com.example.myplayer.model.Playlist
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class MainActivityViewModel @ViewModelInject constructor(
    private val interactor: PlaylistInteractor
) : BaseViewModel() {

    private var _playlistLinks = MutableLiveData<List<Pair<String, String>>>()
    val playlistLinks: LiveData<List<Pair<String, String>>>
        get() = _playlistLinks

    private var _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    fun savePlaylistLinks(playlistName: String, playlistLinks: List<Pair<String, String>>) {
        val playlist = Playlist(playlistName, playlistLinks)
        interactor.savePlaylistLinks(playlist)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onSavePlaylistLinksSuccess, ::onSavePlaylistLinksError)
            .run(compositeDisposable::add)
    }

    private fun onSavePlaylistLinksSuccess() {
        Log.v(TAG, "playlist urls are saved successfully")
    }

    private fun onSavePlaylistLinksError(e: Throwable) {
        _message.value = "Error when saving playlist: " + e.stackTrace.toString()
        Log.e(TAG, e.toString())
    }

    fun loadPlaylistLinks(playlistName: String) {
        interactor.loadPlaylistLinks(playlistName)
            .map { it.playlist }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                _playlistLinks::setValue
            ) { e ->
                _message.setValue("Error when saving playlist: " + e.stackTrace.toString())
                Log.e(TAG, e.message, e)
            }
            .run(compositeDisposable::add)
    }

    fun writeTestData(){
        val testPlaylistName = "Test playlist"
        val testPlaylistLinks = mutableListOf(
            Pair("https://www.youtube.com/watch?v=V4QuVvPjxwk", "https://dump.video/i/Phdch0.mp4"),
            Pair("https://www.youtube.com/watch?v=0CxxS5F-9LQ", "https://dump.video/i/NS5tCP.mp4"),
            Pair("https://www.youtube.com/watch?v=ojubEBKoA3A", "https://dump.video/i/PtxMos.mp4"),
            Pair("https://www.youtube.com/watch?v=gXVXobgdX1k", "https://dump.video/i/XQKTvK.mp4"),
            Pair("https://www.youtube.com/watch?v=KdjieDtkvLw", "https://dump.video/i/DqAggl.mp4"),
            Pair("https://www.youtube.com/watch?v=Xlks46VyUJw", "https://dump.video/i/uxvVLw.mp4"),
            Pair("https://www.youtube.com/watch?v=JVNzqbvS6cE", "https://dump.video/i/S93Y9B.mp4"),
            Pair("https://www.youtube.com/watch?v=dQw4w9WgXcQ", "https://dump.video/i/6O37FT.mp4")
        )
        savePlaylistLinks(testPlaylistName, testPlaylistLinks)
    }
    companion object {
        const val TAG = "MainActivityViewModel"
    }
}