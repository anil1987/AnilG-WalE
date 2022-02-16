package com.nasa.astronomy.base.glide

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.nasa.astronomy.logging.AppLogger

object AppGlide {
    @JvmStatic
    @BindingAdapter(value = ["imageUrl"])
    fun load(
        view: ImageView,
        imageUrl: String?,
    ) {
        AppLogger.e("AppGlide", "imageUrl $imageUrl")
        val requestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .priority(Priority.IMMEDIATE)
            .dontAnimate()
        GlideApp.with(view.context)
            .load(imageUrl)
            .apply(requestOptions)
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    isFirstResource: Boolean
                ): Boolean {
                    // log exception
                    if (e != null) {
                        AppLogger.e("AppGlide", "Error loading image", e)
                    }
                    return false // important to return false so the error placeholder can be placed
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    dataSource: com.bumptech.glide.load.DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            })
            .into(view)
    }
}
