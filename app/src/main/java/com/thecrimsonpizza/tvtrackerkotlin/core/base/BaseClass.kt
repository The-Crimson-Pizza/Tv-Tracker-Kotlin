package com.thecrimsonpizza.tvtrackerkotlin.core.base

import android.os.Parcelable
import java.io.Serializable

interface BaseClass : Serializable, Parcelable {
    val id: Int
    var name: String
    var posterPath: String?
}