package com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.thecrimsonpizza.tvtrackerkotlin.core.base.BaseClass
import kotlinx.android.parcel.Parcelize

class BasicResponse(@SerializedName("results") @Expose var basicSeries: List<SerieBasic>) {

    @Parcelize
    open class SerieBasic(
        override var id: Int,
        override var name: String,
        @SerializedName("poster_path") override var posterPath: String?,
        @SerializedName("vote_average") open var voteAverage: Float = 0.0f
    ) : BaseClass
}