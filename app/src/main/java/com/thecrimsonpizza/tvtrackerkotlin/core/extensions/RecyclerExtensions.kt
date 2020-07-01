package com.thecrimsonpizza.tvtrackerkotlin.core.extensions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.actor.MovieCredits
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.actor.TvCredits
import com.thecrimsonpizza.tvtrackerkotlin.core.base.BaseViewHolder
import com.thecrimsonpizza.tvtrackerkotlin.core.base.SimpleRecyclerAdapter

fun ViewGroup.inflater(layoutRes: Int): View =
    LayoutInflater.from(context).inflate(layoutRes, this, false)

fun <T : Any> RecyclerView.withSimpleAdapter(
    dataList: List<T>, @LayoutRes layoutID: Int,
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

fun RecyclerView.withTvCreditsAdapter(
    dataList: List<TvCredits.Cast>, @LayoutRes layoutID: Int,
    layoutManager: RecyclerView.LayoutManager,
    onBindView: BaseViewHolder<TvCredits.Cast>.(data: TvCredits.Cast) -> Unit
): SimpleRecyclerAdapter<TvCredits.Cast> {
    dataList.sortedWith(nullsLast(compareBy { it.firstAirDate }))
    val recyclerAdapter = SimpleRecyclerAdapter(dataList, layoutID, onBindView)
    adapter = recyclerAdapter
    this.layoutManager = layoutManager
    this.setHasFixedSize(true)
    this.setItemViewCacheSize(20)
    this.isSaveEnabled = true
    return recyclerAdapter
}

fun RecyclerView.withMovieCreditsAdapter(
    dataList: List<MovieCredits.Cast>, @LayoutRes layoutID: Int,
    layoutManager: RecyclerView.LayoutManager,
    onBindView: BaseViewHolder<MovieCredits.Cast>.(data: MovieCredits.Cast) -> Unit
): SimpleRecyclerAdapter<MovieCredits.Cast> {
    dataList.sortedWith(nullsLast(compareBy { it.releaseDate }))
    val recyclerAdapter = SimpleRecyclerAdapter(dataList, layoutID, onBindView)
    adapter = recyclerAdapter
    this.layoutManager = layoutManager
    this.setHasFixedSize(true)
    this.setItemViewCacheSize(20)
    this.isSaveEnabled = true
    return recyclerAdapter
}
