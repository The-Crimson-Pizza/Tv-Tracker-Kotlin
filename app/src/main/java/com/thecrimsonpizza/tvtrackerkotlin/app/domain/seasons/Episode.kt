package com.thecrimsonpizza.tvtrackerkotlin.app.domain.seasons

import android.content.Context
import android.view.View
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.gson.annotations.SerializedName
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.Following
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.checkEpisodesFinished
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.convertToString
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.watchEpisode
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.SEASON_EPISODE_FORMAT
import java.io.Serializable
import java.util.*

data class Episode(
    var id: Int = 0,
    var name: String = "",
    var overview: String = "",
    var followingData: Following = Following(),

    @SerializedName("show_id") var showId: Int = 0,
    @SerializedName("air_date") var airDate: String? = "",
    @SerializedName("episode_number") var episodeNumber: Int = 0,
    @SerializedName("season_number") var seasonNumber: Int = 0,
    @SerializedName("still_path") var stillPath: String? = null,
    @SerializedName("vote_average") var voteAverage: Float = 0f
) : Serializable {

    fun getUnwatchedOverview(context: Context, serie: SerieResponse.Serie): String {
        return when {
            serie.checkEpisodesFinished() -> String.format(
                context.getString(R.string.finished_date),
                serie.followingData.watchedDate.convertToString(GlobalConstants.FORMAT_LONG),
                serie.followingData.watchedDate.convertToString(GlobalConstants.FORMAT_HOURS)
            )
            this.overview.isEmpty() -> context.getString(R.string.no_data)
            else -> this.overview
        }
    }

    fun getUnwatchedTitle(context: Context, serie: SerieResponse.Serie): String {
        return when {
            serie.checkEpisodesFinished() -> context.getString(R.string.just_watch)
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

    fun setWatchedCheck(
        episode: Episode,
        serie: SerieResponse.Serie,
        favs: List<SerieResponse.Serie>,
        watchedCheck: MaterialCheckBox
    ) {
        if (serie.followingData.added) {
            watchedCheck.visibility = View.VISIBLE
            watchedCheck.isChecked = episode.followingData.watched
            watchedCheck.setOnCheckedChangeListener { _: View, isChecked: Boolean ->
                if (isChecked) {
                    if (!episode.followingData.watched) {
                        serie.watchEpisode(favs, true)
                    }
                } else {
                    if (episode.followingData.watched) {
                        serie.watchEpisode(favs, false)
                    }
                }
            }
        }
    }


}
