package com.thecrimsonpizza.tvtrackerkotlin.app.domain.seasons

import android.content.Context
import com.google.gson.annotations.SerializedName
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.FollowingSerie
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.convertToString
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.SEASON_EPISODE_FORMAT
import java.io.Serializable
import java.util.*

data class Episode(
    var id: Int = 0,
    var name: String = "",
    var overview: String = "",

    @SerializedName("show_id") var showId: Int = 0,
    @SerializedName("air_date") var airDate: String? = "",
    @SerializedName("episode_number") var episodeNumber: Int = 0,
    @SerializedName("season_number") var seasonNumber: Int = 0,
    @SerializedName("still_path") var stillPath: String? = null,
    @SerializedName("vote_average") var voteAverage: Float = 0f
) : Serializable {

    fun toEpisodeFollowingData(): FollowingSerie.FollowingData {
        return FollowingSerie.FollowingData(this.id, this.seasonNumber)
    }

//    fun getEpisodeUnwatched(serie: SerieResponse.Serie, dataList: List<FollowingSerie>): Episode? {
//        val data = serie.getFollowingSerie(dataList)
//        val id = data?.data?.flatMap { it.value }?.firstOrNull { !it.watched }?.id
//        return serie.seasons.flatMap { it.episodes }.first { it.id == id }
//    }

    fun getUnwatchedOverview(context: Context, serie: SerieResponse.Serie): String {
        val episode = serie.firstEpisodeUnwatched
        return when {
            serie.followingData?.areAllEpisodesWatched() ?: false ->
                String.format(
                    context.getString(R.string.finished_date),
                    serie.followingData?.finishedDate.convertToString(GlobalConstants.FORMAT_LONG),
                    serie.followingData?.finishedDate.convertToString(GlobalConstants.FORMAT_HOURS)
                )
            episode == null || episode.overview.isEmpty() -> context.getString(R.string.no_data)
            else -> episode.overview
        }
    }


    fun getUnwatchedTitle(context: Context, serie: SerieResponse.Serie): String {
        return when {
            serie.followingData?.areAllEpisodesWatched()
                ?: false -> context.getString(R.string.just_watch)
            this.name.isEmpty() -> context.getString(R.string.no_data)
            else -> String.format(
                Locale.getDefault(),
                SEASON_EPISODE_FORMAT,
                this.seasonNumber,
                this.episodeNumber,
                this.name
            )
        }
    }


}
