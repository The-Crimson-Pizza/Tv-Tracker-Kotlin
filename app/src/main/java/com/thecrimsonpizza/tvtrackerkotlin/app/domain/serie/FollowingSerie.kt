package com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie

import com.google.gson.annotations.SerializedName
import com.thecrimsonpizza.tvtrackerkotlin.core.base.BaseClass
import kotlinx.android.parcel.Parcelize

@Parcelize
class FollowingSerie(
    override var id:Int,
    override var name: kotlin.String,
    @SerializedName("poster_path") override var posterPath: String?,

    var serieFollowingData: FollowingData = FollowingData()

):BaseClass {
}