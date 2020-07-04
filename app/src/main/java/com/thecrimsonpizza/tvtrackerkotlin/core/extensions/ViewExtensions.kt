package com.thecrimsonpizza.tvtrackerkotlin.core.extensions

import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.thecrimsonpizza.tvtrackerkotlin.R


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
