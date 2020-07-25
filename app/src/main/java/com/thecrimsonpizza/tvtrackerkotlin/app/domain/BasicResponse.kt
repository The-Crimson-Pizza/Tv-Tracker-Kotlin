package com.thecrimsonpizza.tvtrackerkotlin.app.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BasicResponse(@SerializedName("results") @Expose var basicSeries: List<SerieBasic>) {

    class SerieBasic(
        val id: Int,
        val name: String,
        @SerializedName("poster_path") val posterPath: String?,
        @SerializedName("vote_average") var voteAverage: Float = 0.0f
    )
}