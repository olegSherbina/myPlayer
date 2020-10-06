package com.example.myplayer.ui.mainactivity.viewmodel

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myplayer.core.base.BaseViewModel
import javax.inject.Inject
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel


class MainActivityViewModel @ViewModelInject constructor(
    //TODO interactor
) : ViewModel() {

    private var _thumbnailURLs = MutableLiveData<MutableList<String>>()
    val thumbnailURLs: LiveData<MutableList<String>>
        get() = _thumbnailURLs

    private val tmpVideoLinks = mutableListOf(
        "https://www.youtube.com/watch?v=V4QuVvPjxwk",
        "https://www.youtube.com/watch?v=0CxxS5F-9LQ",
        "https://www.youtube.com/watch?v=ojubEBKoA3A",
        "https://www.youtube.com/watch?v=gXVXobgdX1k",
        "https://www.youtube.com/watch?v=KdjieDtkvLw",
        "https://www.youtube.com/watch?v=Xlks46VyUJw",
        "https://www.youtube.com/watch?v=JVNzqbvS6cE", //maxresdefault is unavailable
        "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
    )

    fun getThumbnailUrls() {
        _thumbnailURLs.value = tmpVideoLinks
    }
}