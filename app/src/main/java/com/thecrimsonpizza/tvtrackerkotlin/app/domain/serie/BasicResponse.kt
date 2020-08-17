package com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie

import com.google.gson.annotations.SerializedName
import com.thecrimsonpizza.tvtrackerkotlin.core.base.BaseClass
import kotlinx.android.parcel.Parcelize

class BasicResponse(

    @SerializedName("results") var basicSeries: List<SerieBasic>,
    val page: Int,
    @SerializedName("total_results") val totalResults: Int,
    @SerializedName("total_pages") val totalPages: Int

)


{

    @Parcelize
    open class SerieBasic(
        override val id: Int,
        override var name: String,
        @SerializedName("poster_path") override var posterPath: String?,
        @SerializedName("vote_average") val voteAverage: Float = 0.0f
    ) : BaseClass
}