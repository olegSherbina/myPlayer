package com.example.myplayer.ui.recyclerview.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.myplayer.R

typealias OnRecyclerViewItemClickListener = (recyclerView: RecyclerView, position: Int, v: View) -> Unit
typealias OnRecyclerViewItemLongClickListener = (recyclerView: RecyclerView, position: Int, v: View) -> Boolean

class ItemClickSupport private constructor(private val recyclerView: RecyclerView) {

    private var onItemClickListener: OnRecyclerViewItemClickListener? = null
    private var onItemLongClickListener: OnRecyclerViewItemLongClickListener? = null

    private val attachListener: RecyclerView.OnChildAttachStateChangeListener =
        object : RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewAttachedToWindow(view: View) {
                val holder = this@ItemClickSupport.recyclerView.getChildViewHolder(view)
                    .takeIf { it is ItemClickSupportViewHolder } as? ItemClickSupportViewHolder

                if (onItemClickListener != null && holder?.isClickable != false) {
                    view.setOnClickListener(onClickListener)
                }
                if (onItemLongClickListener != null && holder?.isLongClickable != false) {
                    view.setOnLongClickListener(onLongClickListener)
                }
            }

            override fun onChildViewDetachedFromWindow(view: View) {}
        }

    init {
        this.recyclerView.setTag(R.id.item_click_support, this)
        this.recyclerView.addOnChildAttachStateChangeListener(attachListener)
    }

    companion object {
        fun addTo(view: RecyclerView): ItemClickSupport {
            var support: ItemClickSupport? =
                view.getTag(R.id.item_click_support) as? ItemClickSupport
            if (support == null) {
                support = ItemClickSupport(view)
            }
            return support
        }

        fun removeFrom(view: RecyclerView): ItemClickSupport? {
            val support = view.getTag(R.id.item_click_support) as? ItemClickSupport
            support?.detach(view)
            return support
        }
    }

    private val onClickListener = View.OnClickListener { v ->
        val listener = onItemClickListener ?: return@OnClickListener
        val holder = this.recyclerView.getChildViewHolder(v)
        listener.invoke(this.recyclerView, holder.adapterPosition, v)
    }

    private val onLongClickListener = View.OnLongClickListener { v ->
        val listener = onItemLongClickListener ?: return@OnLongClickListener false
        val holder = this.recyclerView.getChildViewHolder(v)
        return@OnLongClickListener listener.invoke(this.recyclerView, holder.adapterPosition, v)
    }

    private fun detach(view: RecyclerView) {
        view.removeOnChildAttachStateChangeListener(attachListener)
        view.setTag(R.id.item_click_support, null)
    }

    fun onItemClick(listener: OnRecyclerViewItemClickListener?): ItemClickSupport {
        onItemClickListener = listener
        return this
    }

    fun onItemLongClick(listener: OnRecyclerViewItemLongClickListener?): ItemClickSupport {
        onItemLongClickListener = listener
        return this
    }
}

interface ItemClickSupportViewHolder {
    val isClickable: Boolean get() = true
    val isLongClickable: Boolean get() = true
}

fun RecyclerView.addItemClickSupport(configuration: ItemClickSupport.() -> Unit = {}) =
    ItemClickSupport.addTo(this).apply(configuration)

fun RecyclerView.removeItemClickSupport() = ItemClickSupport.removeFrom(this)

fun RecyclerView.onItemClick(onClick: OnRecyclerViewItemClickListener) {
    addItemClickSupport { onItemClick(onClick) }
}

fun RecyclerView.onItemLongClick(onLongClick: OnRecyclerViewItemLongClickListener) {
    addItemClickSupport { onItemLongClick(onLongClick) }
}
