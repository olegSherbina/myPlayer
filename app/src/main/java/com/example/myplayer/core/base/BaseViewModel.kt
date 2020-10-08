package com.example.myplayer.core.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel internal constructor() : ViewModel() {
    protected val compositeDisposable: CompositeDisposable = CompositeDisposable()

    public override fun onCleared() {
        this.compositeDisposable.clear()
    }
}
