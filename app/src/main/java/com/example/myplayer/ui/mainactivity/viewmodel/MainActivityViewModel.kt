package com.example.myplayer.ui.mainactivity.viewmodel

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myplayer.core.base.BaseViewModel
import javax.inject.Inject
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel


class MainActivityViewModel @ViewModelInject constructor(
    //TODO interactors, mappers n stuff
) : ViewModel() {

    private var _thumbnail = MutableLiveData<Drawable>()
    val thumbnail: LiveData<Drawable>
        get() = _thumbnail

    fun getThumbnail(videoURL: String){

    }
}