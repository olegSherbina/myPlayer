package com.example.myplayer.ui.recyclerview.viewholders

import android.view.View
import android.view.ViewGroup
import com.example.myplayer.R
import com.example.myplayer.core.utils.inflate
import com.example.myplayer.network.ThumbnailDownloader
import com.example.myplayer.ui.recyclerview.base.BaseViewHolder
import kotlinx.android.synthetic.main.holder_preview_item.view.*

class PreviewsViewHolder(view: View) : BaseViewHolder<String>(view) {
    internal val thumbnailView = view.thumbnailView

    override fun bind(item: String) {
        ThumbnailDownloader.loadThumbnail(item, thumbnailView)
    }

    companion object {
        fun from(parent: ViewGroup) =
            PreviewsViewHolder(
                parent.inflate(R.layout.holder_preview_item)
            )
    }
}