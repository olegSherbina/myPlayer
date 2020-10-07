package com.example.myplayer.core.utils

import android.app.PendingIntent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.ui.PlayerNotificationManager.BitmapCallback
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target


class DescriptionAdapter(
    val thumbnailUrl: String
) : PlayerNotificationManager.MediaDescriptionAdapter {
    override fun getCurrentContentTitle(player: Player): String {
        return "Title"
    }

    override fun getCurrentContentText(player: Player): String? {
        return "ContentText"
    }

    override fun getCurrentLargeIcon(player: Player, callback: BitmapCallback): Bitmap? {
        var result: Bitmap? = null
        val target = object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                result = bitmap
            }
            override fun onBitmapFailed(e: Exception, errorDrawable: Drawable?) {}
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
        }
        Picasso.get().load(thumbnailUrl).into(target)
        return result
    }

    override fun createCurrentContentIntent(player: Player): PendingIntent? {
        return null
    }

}