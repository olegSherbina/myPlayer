package com.example.myplayer.ui.recyclerview.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.myplayer.R


class PreviewsRecyclerViewAdapter(
    private val imageViews: Array<Drawable?> //TODO video previews here
) :
    RecyclerView.Adapter<PreviewsRecyclerViewAdapter.ViewHolder>() {

    open class ViewHolder(itemHolderView: View) : RecyclerView.ViewHolder(itemHolderView) {
        val imageView: ImageView = itemHolderView.findViewById(R.id.tmpImageView)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PreviewsRecyclerViewAdapter.ViewHolder {
        val item: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.tmp_image_item, null)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        item.layoutParams = layoutParams
        return ViewHolder(item)
    }

    override fun getItemCount() = imageViews.size

    override fun onBindViewHolder(holder: PreviewsRecyclerViewAdapter.ViewHolder, position: Int) {
        holder.imageView.minimumWidth
        holder.imageView.setImageDrawable(imageViews[position])
    }
}