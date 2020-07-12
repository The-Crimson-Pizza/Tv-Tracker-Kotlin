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


data class SerieResponse(
    @Expose @SerializedName("results") val results: List<Serie> = listOf()
) {

    data class Serie(
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
        var video: VideoResponse.Video? = null,
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
        @SerializedName("last_episode_to_air") var lastEpisodeToAir: Episode? = null,

        var followingData: Following = Following()

    ) : Serializable {

        fun withSeasons(seasonsList: MutableList<Season>, serie: Serie): Serie {
            serie.seasons = seasonsList
            serie.seasons.sort()
            return serie
        }

        fun updateObject(serie: Serie): Serie? {
            if (this.id == serie.id) {
                if (this != serie) {
                    val following = serie.followingData
                    val seasonMap = HashMap<Int, Following>()
                    val episodeMap = HashMap<Int, Following>()

                    for (season in serie.seasons) {
                        seasonMap[season.id] = season.followingData
                        season.episodes.forEach { episodeMap[it.id] = it.followingData }
                    }
                    val new = this

                    new.followingData = following
                    new.seasons.forEach { season ->
                        season.followingData = seasonMap[season.id]!!;
                        season.episodes.forEach { episode ->
                            episode.followingData = episodeMap[episode.id]!!
                        }
                    }
                    return new
                } else return serie
            } else return null
        }

        fun mostWatchedTvShow(followingList: List<Serie>): String? {
            val seriesMap = HashMap<String, Int>()
            for (tvShow in followingList)
                seriesMap[tvShow.name] =
                    tvShow.seasons.flatMap { it.episodes }.count { it.followingData.watched }
            seriesMap.maxBy { it.value }?.key?.let {
                return it
            } ?: return GlobalConstants.EMPTY_STRING
        }

        fun checkFav(followingList: List<Serie>) {
            val s: Serie? = followingList.filter { it.id == this.id }.singleOrNull()
            if (s != null) {
                s.also { setSeasonWatched(it) }.followingData.added = true
            }
        }

        private fun setSeasonWatched(serie: Serie) {
            var cont = 0
            for (i in serie.seasons.indices) {
                seasons[i].followingData.watched = serie.seasons[i].followingData.watched
                seasons[i].followingData.watchedDate = serie.seasons[i].followingData.watchedDate
                setEpisodeWatched(serie, i)
                if (seasons[i].followingData.watched) cont++
            }
            isSerieFinished(cont)
        }

        private fun setEpisodeWatched(serie: Serie, i: Int) {
            if (serie.seasons.size == seasons.size) {
                for (j in 0 until serie.seasons[i].episodes.size) {
                    if (seasons[i].episodes.size == serie.seasons[i].episodes.size) {
                        seasons[i].episodes[j].followingData.watched =
                            serie.seasons[i].episodes[j].followingData.watched
                        seasons[i].episodes[j].followingData.watchedDate =
                            serie.seasons[i].episodes[j].followingData.watchedDate
                    }
                }
            }
            if (checkAllEpisodes(serie.seasons[i].episodes)) {
                seasons[i].followingData.watched = true
                seasons[i].followingData.watchedDate =
                    getMaxDate(getDatesEpisodes(serie.seasons[i].episodes))
            } else {
                seasons[i].followingData.watched = false
                seasons[i].followingData.watchedDate = null
            }
        }

        private fun isSerieFinished(cont: Int) {
            if (seasons.size == cont) {
                followingData.watched = true
                followingData.watchedDate = getMaxDate(getDatesSeason(seasons))
            } else {
                followingData.watched = false
                followingData.watchedDate = null
            }
        }

        private fun checkAllEpisodes(episodes: List<Episode>): Boolean {
            var cont = 0
            for (i in episodes.indices) {
                if (episodes[i].followingData.watched) {
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
                if (e.followingData.watchedDate != null) {
                    dates.add(e.followingData.watchedDate!!)
                }
            }
            return dates
        }

        private fun getDatesSeason(seasons: List<Season>?): List<Date> {
            val dates: MutableList<Date> =
                ArrayList()
            for (s in seasons!!) {
                if (s.followingData.watchedDate != null) {
                    dates.add(s.followingData.watchedDate!!)
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

        data class Genre(
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

        data class Network(
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
                val CREATOR: Parcelable.Creator<Network> =
                    object : Parcelable.Creator<Network> {
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

        data class ExternalIds(
            @SerializedName("imdb_id") var imdbId: String? = null,
            @SerializedName("instagram_id") var instagramId: String? = null
        ) : Serializable
    }
}
