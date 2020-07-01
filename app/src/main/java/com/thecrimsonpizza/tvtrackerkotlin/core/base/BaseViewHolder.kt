package com.thecrimsonpizza.tvtrackerkotlin.core.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.inflater

class BaseViewHolder<in T>(parent: ViewGroup, @LayoutRes layoutId: Int) :
    RecyclerView.ViewHolder(parent.inflater(layoutId))


