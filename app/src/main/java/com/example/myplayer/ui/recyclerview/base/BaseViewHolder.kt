package com.example.myplayer.ui.recyclerview.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T>(val view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(item: T)
}
