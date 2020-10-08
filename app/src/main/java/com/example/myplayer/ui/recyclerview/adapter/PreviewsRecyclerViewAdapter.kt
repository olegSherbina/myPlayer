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
    private var thumbnailLinks: List<String>
) :
    RecyclerView.Adapter<PreviewsViewHolder>() {

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

    override fun getItemCount() = thumbnailLinks.size

    override fun onBindViewHolder(holder: PreviewsViewHolder, position: Int) {
        holder.bind(thumbnailLinks[position])
    }
}