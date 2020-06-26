package com.thecrimsonpizza.tvtrackerkotlin.app.domain.actor

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TvCredits {

    @Expose
    var cast: List<Cast>? = null

    fun TvCredits() {
//        Empty constructor
    }

    class Cast(
        var id: Int,
        var name: String?,
        var character: String?,
        @SerializedName("original_name") var originalName: String?,
        @SerializedName("episode_count") var episodeCount: Int,
        @SerializedName("poster_path") var posterPath: String?,
        @SerializedName("first_air_date") var firstAirDate: String?,
        @SerializedName("vote_average") var voteAverage: Float
    ) : Serializable
}