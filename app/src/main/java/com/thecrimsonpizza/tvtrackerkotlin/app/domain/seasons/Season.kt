package com.thecrimsonpizza.tvtrackerkotlin.app.domain.seasons

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

class Season(
    var id: Int = 0,
    var name: String?,
    var episodes: List<Episode>?,
    var overview: String?,
    var visto: Boolean = false,
    var watchedDate: Date?,
    @SerializedName("air_date") var airDate: String?,
    @SerializedName("poster_path") var posterPath: String?,
    @SerializedName("season_number") var seasonNumber: Int,
    @SerializedName("episode_count") var episodeCount: Int?
) : Serializable {

    fun sort(seasons: List<Season>){
        seasons.sortedBy { it.seasonNumber }
        // todo - comprobar que funciona la ordenación
    }

}