package com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.actor.Credits
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.seasons.Episode
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.seasons.Season
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.sort
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants
import java.io.Serializable
import java.util.*

class SerieResponse(
    @Expose @SerializedName("results") val results: List<Serie> = listOf()
) {

    class Serie(
        var id: Int = 0,
        var name: String = "",
        var status: String = "",
        var homepage: String = "",
        var overview: String = "",
        var genres: List<Genre> = listOf(),
        var networks: List<Network> = listOf(),
        var credits: Credits = Credits(),
        var similar: Similar = Similar(),
        var seasons: MutableList<Season> = mutableListOf(),
        var video: VideoResponse.Video = VideoResponse.Video(),
        var added: Boolean = false,
//        var finished: Boolean = false,
        var addedDate: Date? = null,
        var finishDate: Date? = null,
        var lastEpisodeWatched: Episode = Episode(),
        @SerializedName("original_name") var originalName: String = "",
        @SerializedName("first_air_date") var firstAirDate: String = "",
        @SerializedName("last_air_date") var lastAirDate: String = "",
        @SerializedName("poster_path") var posterPath: String? = null,
        @SerializedName("backdrop_path") var backdropPath: String? = null,
        @SerializedName("episode_run_time") var episodeRunTime: List<Int> = listOf(),
        @SerializedName("in_production") var inProduction: Boolean = false,
        @SerializedName("number_of_episodes") var numberOfEpisodes: Int = 0,
        @SerializedName("number_of_seasons") var numberOfSeasons: Int = 0,
        @SerializedName("origin_country") var originCountry: List<String> = listOf(),
        @SerializedName("original_language") var originalLanguage: String = "",
        @SerializedName("vote_average") var voteAverage: Float = 0f,
        @SerializedName("external_ids") var externalIds: ExternalIds = ExternalIds(),
        @SerializedName("next_episode_to_air") var nextEpisodeToAir: Episode? = null,
        @SerializedName("last_episode_to_air") var lastEpisodeToAir: Episode? = null
    ) : Serializable {

        var finished: Boolean = false
            set(value) {
                field = value
                finishDate = if (finished) Date() else null
            }

        fun withSeasons(seasonsList: MutableList<Season>, serie: Serie): Serie {
            serie.seasons = seasonsList
            serie.seasons.sort()
            return serie
        }

        fun mostWatchedTvShow(followingList: List<Serie>): String? {
            val seriesMap = HashMap<String, Int>()
            for (tvShow in followingList)
                seriesMap[tvShow.name] = tvShow.seasons.flatMap { it.episodes }.count { it.watched }
            seriesMap.maxBy { it.value }?.key?.let {
                return it
            } ?: return GlobalConstants.EMPTY_STRING
        }

        fun checkFav(followingList: List<Serie>) {
            followingList.first { it.id == this.id }.also { setSeasonWatched(it) }.added = true
//            for (s in seriesFavs) {
////                if (id == s.id) {
////                    added = true
////                    setSeasonWatched(s)
////                    break
////                }
////            }
        }

        private fun setSeasonWatched(serie: Serie) {
            var cont = 0
            for (i in serie.seasons.indices) {
                seasons[i].watched = serie.seasons[i].watched
                seasons[i].watchedDate = serie.seasons[i].watchedDate
                setEpisodeWatched(serie, i)
                if (seasons[i].watched) cont++
            }
            isSerieFinished(cont)
        }

        private fun setEpisodeWatched(serie: Serie, i: Int) {
            for (j in 0 until serie.seasons[i].episodes.size) {
                seasons[i].episodes[j].watched =
                    serie.seasons[i].episodes[j].watched
                seasons[i].episodes[j].watchedDate =
                    serie.seasons[i].episodes[j].watchedDate
            }
            if (checkAllEpisodes(serie.seasons[i].episodes)) {
                seasons[i].watched = true
                seasons[i].watchedDate =
                    getMaxDate(getDatesEpisodes(serie.seasons[i].episodes))
            } else {
                seasons[i].watched = false
                seasons[i].watchedDate = null
            }
        }

        private fun isSerieFinished(cont: Int) {
            if (seasons.size == cont) {
                finished = true
                finishDate = getMaxDate(getDatesSeason(seasons))
            } else {
                finished = false
                finishDate = null
            }
        }

        private fun checkAllEpisodes(episodes: List<Episode>): Boolean {
            var cont = 0
            for (i in episodes.indices) {
                if (episodes[i].watched) {
                    cont++
                }
            }
            return cont == episodes.size
        }

        private fun getMaxDate(datesList: List<Date>): Date? {
            return if (datesList.isEmpty()) {
                null
            } else Collections.max(datesList)
        }

        private fun getDatesEpisodes(episodes: List<Episode>): List<Date> {
            val dates: MutableList<Date> =
                ArrayList()
            for (e in episodes) {
                if (e.watchedDate != null) {
                    dates.add(e.watchedDate!!)
                }
            }
            return dates
        }

        private fun getDatesSeason(seasons: List<Season>?): List<Date> {
            val dates: MutableList<Date> =
                ArrayList()
            for (s in seasons!!) {
                if (s.watchedDate != null) {
                    dates.add(s.watchedDate!!)
                }
            }
            return dates
        }

        fun getPosition(favs: List<Serie>): Int {
            for (i in favs.indices) {
                if (favs[i].id == id) {
                    return i
                }
            }
            return -1
        }

        class Genre(
            var id: Int = 0,
            var name: String = ""
        ) : Parcelable {

            override fun writeToParcel(dest: Parcel?, flags: Int) {
                dest?.let {
                    dest.writeInt(id)
                    dest.writeString(name)
                }
            }

            override fun describeContents(): Int = 0

            companion object {
                @JvmField
                val CREATOR: Parcelable.Creator<Genre> = object : Parcelable.Creator<Genre> {
                    override fun newArray(size: Int): Array<Genre?> = arrayOfNulls(size)
                    override fun createFromParcel(source: Parcel): Genre = Genre(source)
                }
            }

            constructor(source: Parcel) : this(
                source.readInt(),
                source.readString().toString()
            )
        }

        class Network(
            var id: Int = 0,
            var name: String = "",
            @SerializedName("logo_path") var logoPath: String? = ""
        ) : Parcelable {


            override fun writeToParcel(dest: Parcel?, flags: Int) {
                dest?.let {
                    dest.writeInt(id)
                    dest.writeString(name)
                    dest.writeString(logoPath)
                }
            }

            override fun describeContents(): Int = 0

            companion object {
                @JvmField
                val CREATOR: Parcelable.Creator<Network> = object : Parcelable.Creator<Network> {
                    override fun newArray(size: Int): Array<Network?> = arrayOfNulls(size)
                    override fun createFromParcel(source: Parcel): Network = Network(source)
                }
            }

            constructor(source: Parcel) : this(
                source.readInt(),
                source.readString().toString(),
                source.readString().toString()
            )
        }

        class ExternalIds(
            @SerializedName("imdb_id") var imdbId: String? = null,
            @SerializedName("instagram_id") var instagramId: String? = null
        ) : Serializable
    }
}
