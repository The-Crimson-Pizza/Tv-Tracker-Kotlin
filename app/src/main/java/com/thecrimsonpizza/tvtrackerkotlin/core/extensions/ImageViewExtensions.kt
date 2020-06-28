package com.thecrimsonpizza.tvtrackerkotlin.core.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.thecrimsonpizza.tvtrackerkotlin.R

class ImageViewExtensions {
    fun ImageView.getImage(url: String) {
        Glide.with(this.context).load(url)
            .apply(
                RequestOptions()
//                .placeholder(R.drawable.loading_poster)
//                .error(R.drawable.default_poster)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            )
            .into(this)
    }

    fun ImageView.getImagePortrait(url: String) {
        Glide.with(this.context).load(url)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_poster)
                    .error(R.drawable.default_portrait_big)
//                .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            )
            .into(this)
    }

    fun ImageView.getImageNoPlaceholder(
        url: String?
    ) {
        Glide.with(this.context)
            .load(url)
            .into(this)
    }
}