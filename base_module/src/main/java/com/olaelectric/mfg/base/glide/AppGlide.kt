package com.olaelectric.mfg.base.glide

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition

object AppGlide {

    @JvmStatic
    @BindingAdapter(value = ["imageUrl", "errorRes", "placeHolderRes"], requireAll = false)
    fun load(
        view: ImageView,
        imageUrl: String?,
        errorRes: Drawable? = null,
        placeHolderRes: Drawable? = null
    ) {
        val glide = GlideApp.with(view.context)
        when (!TextUtils.isEmpty(imageUrl)) {
            true -> {
                val requestOptions = RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .priority(Priority.IMMEDIATE)
                if (placeHolderRes != null) {
                    glide
                        .load(imageUrl)
                        .placeholder(placeHolderRes)
                        .apply(requestOptions)
                        .error(errorRes)
                        .into(view)
                } else {
                    glide
                        .load(imageUrl)
                        .apply(requestOptions)
                        .error(errorRes)
                        .into(view)
                }
            }
            false -> {
                loadDrawableResource(view, errorRes)
            }
        }
    }

    @SuppressLint("CheckResult")
    fun loadBgView(
        view: View,
        imageUrl: String?,
        errorRes: Drawable?,
        placeHolderRes: Drawable? = null
    ) {
        val glide = GlideApp.with(view.context)
        when (!TextUtils.isEmpty(imageUrl)) {
            true -> {
                val requestOptions = RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .priority(Priority.IMMEDIATE)
                if (placeHolderRes != null) {

                    glide
                        .load(imageUrl)
                        .placeholder(placeHolderRes)
                        .error(errorRes)
                        .apply(requestOptions)
                        .into(object : CustomViewTarget<View, Drawable>(view) {
                            override fun onResourceReady(
                                resource: Drawable,
                                transition: Transition<in Drawable>?
                            ) {
                                view.background = resource
                            }

                            override fun onResourceCleared(placeholder: Drawable?) {}

                            override fun onLoadFailed(errorDrawable: Drawable?) {
                                view.background = errorDrawable
                            }
                        })
                }
            }
        }
    }

    fun loadWithCallback(
        view: ImageView,
        imageUrl: String?,
        errorRes: Drawable?,
        placeHolderRes: Drawable?,
        callBack: () -> Unit
    ) {
        val glide = GlideApp.with(view.context)
        when (!TextUtils.isEmpty(imageUrl)) {
            true -> {
                val requestOptions = RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .priority(Priority.IMMEDIATE)
                if (placeHolderRes != null) {
                    glide
                        .load(imageUrl)
                        .placeholder(placeHolderRes)
                        .apply(requestOptions)
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                callBack()
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                callBack()
                                return false
                            }
                        })
                        .error(errorRes)
                        .into(view)
                }
            }
            false -> {
                loadDrawableResource(view, errorRes)
            }
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["imageUrl", "errorRes", "placeHolderRes"], requireAll = false)
    fun load(view: ImageView, imageUrl: Int, errorRes: Drawable?, placeHolderRes: Drawable?) {
        view.setImageResource(imageUrl)
    }

    private fun loadDrawableResource(posterIv: ImageView, errorRes: Drawable?) {
        errorRes?.let {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .priority(Priority.IMMEDIATE)
                .dontAnimate()

            GlideApp.with(posterIv.context)
                .load(errorRes)
                .apply(requestOptions)
                .into(posterIv)
        }
    }
}
