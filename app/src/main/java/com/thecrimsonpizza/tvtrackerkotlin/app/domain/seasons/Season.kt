package com.thecrimsonpizza.tvtrackerkotlin.app.domain.seasons

import com.google.gson.annotations.SerializedName
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.FollowingData
import java.io.Serializable

data class Season(
    var id: Int = 0,
    var name: String = "",
    var episodes: MutableList<Episode> = mutableListOf(),
    var overview: String = "",
    var followingData: FollowingData = FollowingData(),
    @SerializedName("air_date") var airDate: String = "",
    @SerializedName("poster_path") var posterPath: String? = "",
    @SerializedName("season_number") var seasonNumber: Int = 0,
    @SerializedName("episode_count") var episodeCount: Int = 0
) : Serializable