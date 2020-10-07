package com.example.myplayer.ui.mainactivity.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel


class MainActivityViewModel @ViewModelInject constructor(
    //TODO interactor
) : ViewModel() {

    private var _thumbnailsAndVideosUrl = MutableLiveData<MutableList<Pair<String, String>>>()
    val thumbnailsAndVideosUrl: LiveData<MutableList<Pair<String, String>>>
        get() = _thumbnailsAndVideosUrl


    private val tmpVideoLinks = mutableListOf(
        Pair("https://www.youtube.com/watch?v=V4QuVvPjxwk", "https://dump.video/i/Phdch0.mp4"),
        Pair("https://www.youtube.com/watch?v=0CxxS5F-9LQ", "https://dump.video/i/NS5tCP.mp4"),
        Pair("https://www.youtube.com/watch?v=ojubEBKoA3A", "https://dump.video/i/PtxMos.mp4"),
        Pair("https://www.youtube.com/watch?v=gXVXobgdX1k", "https://dump.video/i/XQKTvK.mp4"),
        Pair("https://www.youtube.com/watch?v=KdjieDtkvLw", "https://dump.video/i/DqAggl.mp4"),
        Pair("https://www.youtube.com/watch?v=Xlks46VyUJw", "https://dump.video/i/uxvVLw.mp4"),
        Pair("https://www.youtube.com/watch?v=JVNzqbvS6cE", "https://dump.video/i/S93Y9B.mp4"),
        Pair("https://www.youtube.com/watch?v=dQw4w9WgXcQ", "https://dump.video/i/6O37FT.mp4")
    )

    fun getThumbnailUrls() {
        _thumbnailsAndVideosUrl.value = tmpVideoLinks
    }
}