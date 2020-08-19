package com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.actor.Credits
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.seasons.Episode
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.seasons.Season
import com.thecrimsonpizza.tvtrackerkotlin.core.base.BaseClass
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
import java.util.*


data class SerieResponse(
    @Expose @SerializedName("results") val results: List<Serie> = listOf()
) {

    @Parcelize
    data class Serie(
        var status: String = "",
        var homepage: String = "",
        var overview: String = "",
        var genres: List<Genre> = listOf(),
        var networks: List<Network> = listOf(),
        var credits: Credits = Credits(),
        var similar: Similar = Similar(),
        var seasons: List<Season> = listOf(),
        var video: VideoResponse.Video? = null,

        var lastEpisodeWatched: Episode? = null,
        var firstEpisodeUnwatched: Episode? = null,

        var followingData: FollowingSerie? = null,

        @SerializedName("first_air_date") var firstAirDate: String? = "",
        @SerializedName("original_name") var originalName: String = "",
        @SerializedName("last_air_date") var lastAirDate: String = "",
        @SerializedName("backdrop_path") var backdropPath: String? = null,
        @SerializedName("episode_run_time") var episodeRunTime: List<Int> = listOf(),
        @SerializedName("in_production") var inProduction: Boolean = false,
        @SerializedName("number_of_episodes") var numberOfEpisodes: Int = 0,
        @SerializedName("number_of_seasons") var numberOfSeasons: Int = 0,
        @SerializedName("origin_country") var originCountry: List<String> = listOf(),
        @SerializedName("original_language") var originalLanguage: String = "",
        @SerializedName("external_ids") var externalIds: ExternalIds = ExternalIds(),
        @SerializedName("next_episode_to_air") var nextEpisodeToAir: Episode? = null,
        @SerializedName("last_episode_to_air") var lastEpisodeToAir: Episode? = null

    ) : BasicResponse.SerieBasic(0, "", "", 0f) {


        fun getSerieFromFollowingList(list: List<Serie>): Serie? {
            return list.find { it.id == this.id }
        }

        fun episodesToMap(): Map<String, MutableList<FollowingSerie.FollowingData>> {
            val episodeList = mutableMapOf<String, MutableList<FollowingSerie.FollowingData>>()
            this.seasons.forEach {
                val id = "${it.seasonNumber}_"
                episodeList[id] = it.toSeasonFollowingDataList()
            }
            return episodeList
        }

        fun getEpisode(idEpisode: Int): Episode? {
            return this.seasons.flatMap { it.episodes }.find { it.id == idEpisode }
        }


        fun mostWatchedTvShow(followingList: List<Serie>): String? {
            val seriesMap = HashMap<String, Int>()
//            this.seasons.forEach { seasonList.add(it.toSeasonFollowingData()) }
            for (tvShow in followingList)
                seriesMap[tvShow.name] =
                    tvShow.followingData?.episodesData?.flatMap { it.value }?.count { it.watched }?:0
            seriesMap.maxBy { it.value }?.key?.let {
                return it
            } ?: return GlobalConstants.EMPTY_STRING
        }


//        private fun checkAllEpisodes(episodes: List<Episode>): Boolean {
//            var cont = 0
//            for (i in episodes.indices) {
//                if (episodes[i].followingData.watched) {
//                    cont++
//                }
//            }
//            return cont == episodes.size
//        }

        private fun getMaxDate(datesList: List<Date>): Date? {
            return if (datesList.isEmpty()) {
                null
            } else Collections.max(datesList)
        }

//        private fun getDatesEpisodes(episodes: List<Episode>): List<Date> {
//            val dates: MutableList<Date> =
//                ArrayList()
//            for (e in episodes) {
//                if (e.followingData.watchedDate != null) {
//                    dates.add(e.followingData.watchedDate!!)
//                }
//            }
//            return dates
//        }

//        private fun getDatesSeason(seasons: List<Season>?): List<Date> {
//            val dates: MutableList<Date> =
//                ArrayList()
//            for (s in seasons!!) {
//                if (s.followingData.watchedDate != null) {
//                    dates.add(s.followingData.watchedDate!!)
//                }
//            }
//            return dates
//        }

        fun getPosition(favs: List<Serie>): Int {
            for (i in favs.indices) {
                if (favs[i].id == id) {
                    return i
                }
            }
            return -1
        }

        @Parcelize
        data class Genre(
            override val id: Int = 0,
            override var name: String = "",
            override var posterPath: String? = null
        ) : BaseClass

        @Parcelize
        data class Network(
            override val id: Int = 0,
            override var name: String = "",
            @SerializedName("logo_path") override var posterPath: String? = ""
        ) : BaseClass

        data class ExternalIds(
            @SerializedName("imdb_id") var imdbId: String? = null,
            @SerializedName("instagram_id") var instagramId: String? = null
        ) : Serializable
    }
}
