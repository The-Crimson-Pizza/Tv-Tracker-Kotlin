package com.thecrimsonpizza.tvtrackerkotlin.core.base

import android.os.Parcelable
import java.io.Serializable

interface BaseClass : Serializable, Parcelable {
    var id: Int
    var name: String
    var posterPath: String?
}