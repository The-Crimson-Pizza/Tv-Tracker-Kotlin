package com.thecrimsonpizza.tvtrackerkotlin.app.domain.seasons

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

class Season(
    var id: Int = 0,
    var name: String?,
    var episodes: MutableList<Episode>,
    var overview: String?,
//    var watched: Boolean = false,
    var watchedDate: Date?,
    @SerializedName("air_date") var airDate: String?,
    @SerializedName("poster_path") var posterPath: String?,
    @SerializedName("season_number") var seasonNumber: Int,
    @SerializedName("episode_count") var episodeCount: Int?
) : Serializable {

    var watched: Boolean = false
        set(value) {
            field = value
            watchedDate = if (watched) Date() else null
        }


    fun sort(seasons: MutableList<Season>) {
        seasons.sortedBy { it.seasonNumber }
        // todo - comprobar que funciona la ordenación
    }

}