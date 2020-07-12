package com.thecrimsonpizza.tvtrackerkotlin.core.extensions

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.thecrimsonpizza.tvtrackerkotlin.R

fun ImageView.getImage(context: Context, url: String?) {
    Glide.with(context).load(url)
        .apply(
            RequestOptions()
                .placeholder(R.drawable.loading_poster)
                .error(R.drawable.default_poster)
//                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        )
        .dontAnimate()
        .into(this)
}

fun ImageView.getImagePortrait(context: Context, url: String) {
    Glide.with(context).load(url)
        .apply(
            RequestOptions()
                .placeholder(R.drawable.loading_poster)
                .error(R.drawable.default_portrait_big)
//                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        )
        .dontAnimate()
        .into(this)
}

fun ImageView.getImageNoPlaceholder(context: Context, url: String) {
    Glide.with(context)
        .load(url)
        .dontAnimate()
        .into(this)
}
