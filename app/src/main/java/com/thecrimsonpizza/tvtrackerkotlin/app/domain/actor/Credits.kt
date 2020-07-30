package com.thecrimsonpizza.tvtrackerkotlin.app.domain.actor

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.thecrimsonpizza.tvtrackerkotlin.core.base.BaseClass
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

data class Credits(@Expose var cast: List<Cast> = mutableListOf()) : Serializable {

    @Parcelize
    data class Cast(
        override var id: Int = 0,
        override var name: String = "",
        var character: String = "",

        @SerializedName("episode_count") var episodeCount: Int = 0,
        @SerializedName("poster_path") override var posterPath: String? = null,
        @SerializedName("first_air_date") var firstAirDate: String = "",
        @SerializedName("vote_average") var voteAverage: Float = 0f,

        @SerializedName("profile_path") var profilePath: String? = null,

        var title: String = "",
        @SerializedName("original_title") var originalTitle: String = "",
        @SerializedName("release_date") var releaseDate: String = ""

    ) : BaseClass
}