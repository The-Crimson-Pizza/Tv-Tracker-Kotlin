package com.thecrimsonpizza.tvtrackerkotlin.core.extensions

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

fun RecyclerView.setUp(
    layoutManager: RecyclerView.LayoutManager,
    adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
) {
    this.layoutManager = layoutManager
    this.adapter = adapter
}

fun TextView.setTextAndHideViewIfIsNeeded(text: String) {
    if (!TextUtils.isEmpty(text)) {
        this.text = text
        this.visibility = View.VISIBLE
    } else {
        this.text = ""
        this.visibility = View.GONE
    }
}

fun View.isVisible(): Boolean {
    return this.visibility == View.VISIBLE
}

fun ImageView.loadUrl(url: String) {
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

fun ImageView.getImageNoPlaceholder(
    url: String?
) {
    Glide.with(this.context)
        .load(url)
        .into(this)
}