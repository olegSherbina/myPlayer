package com.example.myplayer.ui.recyclerview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.myplayer.R
import com.example.myplayer.ui.recyclerview.viewholders.PreviewsViewHolder
import com.squareup.picasso.Picasso


class PreviewsRecyclerViewAdapter(
    private var thumbnailLinks: List<String> //TODO video previews here
) :
    RecyclerView.Adapter<PreviewsViewHolder>() {

    /*open class ViewHolder(itemHolderView: View) : RecyclerView.ViewHolder(itemHolderView) {
        val imageView: ImageView = itemHolderView.findViewById(R.id.thumbnailView)

    }*/

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PreviewsViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.holder_preview_item, parent, false)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        view.layoutParams = layoutParams
        return PreviewsViewHolder(view)
    }


    /*val item: View = LayoutInflater.from(parent.context)
        .inflate(R.layout.holder_preview_item, null)
    val layoutParams = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.MATCH_PARENT
    )
    item.layoutParams = layoutParams
    return ViewHolder(item)*/


    override fun getItemCount() = thumbnailLinks.size

    override fun onBindViewHolder(holder: PreviewsViewHolder, position: Int) {
        /*//ThumbnailDownloader.loadThumbnail(imageViews[position], holder.imageView)
        Picasso.get().isLoggingEnabled = true
        Picasso.get().load(thumbnailLinks[position]).into(holder.thumbnailView)*/
        holder.bind(thumbnailLinks[position])
    }
}