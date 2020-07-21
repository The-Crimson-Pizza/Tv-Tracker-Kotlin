package com.thecrimsonpizza.tvtrackerkotlin.core.extensions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.thecrimsonpizza.tvtrackerkotlin.core.base.BaseViewHolder
import com.thecrimsonpizza.tvtrackerkotlin.core.base.SimpleRecyclerAdapter

fun ViewGroup.inflater(layoutRes: Int): View =
    LayoutInflater.from(context).inflate(layoutRes, this, false)

fun <T : Any> RecyclerView.setBaseAdapter(
    dataList: List<T>,
    @LayoutRes layoutID: Int,
    layoutManager: RecyclerView.LayoutManager,
    onBindView: BaseViewHolder<T>.(data: T) -> Unit
): SimpleRecyclerAdapter<T> {
    val recyclerAdapter = SimpleRecyclerAdapter(dataList, layoutID, onBindView)
    adapter = recyclerAdapter
    this.layoutManager = layoutManager
    this.setHasFixedSize(true)
    this.setItemViewCacheSize(20)
    this.isSaveEnabled = true
    return recyclerAdapter
}
