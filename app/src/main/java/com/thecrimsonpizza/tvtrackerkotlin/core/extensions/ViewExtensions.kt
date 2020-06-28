package com.thecrimsonpizza.tvtrackerkotlin.core.extensions

import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.setUp(
    layoutManager: RecyclerView.LayoutManager,
    adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
) {
    this.layoutManager = layoutManager
    this.adapter = adapter
    this.setHasFixedSize(true)
    this.setItemViewCacheSize(20)
    this.isSaveEnabled = true
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

