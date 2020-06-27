package com.thecrimsonpizza.tvtrackerkotlin.app.domain.seasons

import android.view.View
import com.google.gson.annotations.SerializedName
import com.thecrimsonpizza.tvtrackerkotlin.app.data.local.FirebaseDb
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse
import java.io.Serializable
import java.util.*

class Episode(
    var id: Int,
    var name: String,
    var overview: String?,
    var visto: Boolean = false,
    var watchedDate: Date?,
    @SerializedName("show_id") var showId: Int,
    @SerializedName("air_date") var airDate: String,
    @SerializedName("episode_number") var episodeNumber: Int,
    @SerializedName("season_number") var seasonNumber: Int,
    @SerializedName("still_path") var stillPath: String?,
    @SerializedName("vote_average") var voteAverage: Float?
) : Serializable {

    /**
     * Checks if the episode is watched and sets it true or false the CheckBox
     *
     * @param episode   episode of the season
     * @param serie     serie loaded in the SerieFragment
     * @param favs      list of series in the following list
     * @param seasonPos position of the season in the season list
     */
    fun setWatchedCheck(
        episode: Episode,
        serie: SerieResponse.Serie,
        favs: List<SerieResponse.Serie>,
        seasonPos: Int,
        episodePos: Int,
        watchedCheck: MaterialCheckBox
    ) {
        if (serie.added) {
            watchedCheck.setVisibility(View.VISIBLE)
            watchedCheck.setChecked(episode.visto)
            watchedCheck.setOnCheckedChangeListener({ _: View, isChecked: Boolean ->
                if (isChecked) {
                    if (!episode.visto) {
                        watchEpisode(serie, favs, episodePos, seasonPos)
                    }
                } else {
                    if (episode.visto) {
                        unwatchEpisode(serie, favs, episodePos, seasonPos)
                    }
                }
            })
        }
    }

    /**
     * Set as watched the episode in the RecyclerView and then in the Database
     *
     * @param serie      serie loaded in the SerieFragment
     * @param favs       list of series in the following list
     * @param episodePos episode position in the RecyclerView
     * @param seasonPos  position of the season in the season list
     */
    private fun watchEpisode(
        serie: SerieResponse.Serie,
        favs: List<SerieResponse.Serie>,
        episodePos: Int,
        seasonPos: Int
    ) {
        val favPosition: Int = serie.getPosition(favs)
        if (favPosition != -1) {
            favs[favPosition].seasons.get(seasonPos).episodes.get(episodePos).visto = true
            favs[favPosition].seasons.get(seasonPos).episodes.get(episodePos).watchedDate =
                Date()
            if (checkEpisodesFinished(favs[favPosition])) {
                favs[favPosition].seasons.get(seasonPos).visto = true
                favs[favPosition].seasons.get(seasonPos).watchedDate = Date()
            } else {
                favs[favPosition].seasons.get(seasonPos).visto = false
                favs[favPosition].seasons.get(seasonPos).watchedDate = null
            }
            if (checkSeasonFinished(favs[favPosition])) {
                favs[favPosition].finished = true
                favs[favPosition].finishDate = Date()
            } else {
                favs[favPosition].finished = false
                favs[favPosition].finishDate = null
            }
            FirebaseDb.getInstance(FirebaseAuth.getInstance().getCurrentUser()).setSeriesFav(favs)
        }
    }

    /**
     * Set as unwatched the episode in the RecyclerView and the in the Database
     *
     * @param serie      serie loaded in the SerieFragment
     * @param favs       list of series in the following list
     * @param episodePos episode position in the RecyclerView
     * @param seasonPos  position of the season in the season list
     */
    private fun unwatchEpisode(
        serie: SerieResponse.Serie,
        favs: List<SerieResponse.Serie>,
        episodePos: Int,
        seasonPos: Int
    ) {
        val favPosition: Int = serie.getPosition(favs)
        if (favPosition != -1) {
            favs[favPosition].seasons.get(seasonPos).episodes.get(episodePos).visto = false
            favs[favPosition].seasons.get(seasonPos).episodes.get(episodePos).watchedDate = null
            favs[favPosition].seasons.get(seasonPos).visto = false
            favs[favPosition].seasons.get(seasonPos).watchedDate = null
            favs[favPosition].finished = false
            favs[favPosition].finishDate = null
            FirebaseDb.getInstance(FirebaseAuth.getInstance().getCurrentUser()).setSeriesFav(favs)
        }
    }

    /**
     * Checks if the season is finished or not
     *
     * @param serie we need its seasons
     * @return true or false
     */
    private fun checkSeasonFinished(serie: SerieResponse.Serie): Boolean {
        for (s in serie.seasons) {
            if (!s.visto) return false
        }
        return true
    }

    /**
     * Checks if the episodes are watched so the season is finished or not
     *
     * @param serie we need its seasons and episodes
     * @return true or false
     */
    private fun checkEpisodesFinished(serie: SerieResponse.Serie): Boolean {
        for (s in serie.seasons) {
            for (e in s.episodes!!) {
                if (!e.visto) return false
            }
        }
        return true
    }
}
