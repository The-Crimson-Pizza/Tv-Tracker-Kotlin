package com.thecrimsonpizza.tvtrackerkotlin.app.domain.actor

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MovieCredits {

    @Expose
    var cast: List<Cast>? = null

    fun MovieCredits() {
//        Empty constructor for Firebase serialize
    }

    class Cast(
        var id: Int = 0,
        var title: String?,
        var character: String?,
        @SerializedName("poster_path") var posterPath: String?,
        @SerializedName("original_title") var originalTitle: String?,
        @SerializedName("release_date") var releaseDate: String?,
        @SerializedName("vote_average") var voteAverage: Float?
    ) : Serializable
}