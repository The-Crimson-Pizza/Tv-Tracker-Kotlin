package com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie

import android.os.Parcelable
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.seasons.Episode
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
import java.util.*

@Parcelize
data class FollowingSerie(

    var finishedDate: Date? = null,
    var addedDate: Date? = null,
    var episodesData: Map<String, MutableList<FollowingData>> = mapOf()

) : Parcelable, Serializable {

    var added: Boolean = false
        set(value) {
            field = value
            addedDate =
                if (added && addedDate == null) Date() else if (!added) null else addedDate
        }

    var finished: Boolean = false
        set(value) {
            field = value
            finishedDate =
                if (finished && finishedDate == null) Date() else if (!finished) null else finishedDate
        }

    @Parcelize
    data class FollowingData(
        val id: Int = 0,
        val seasonNumber: Int = 0,
        var watchedDate: Date? = null
    ) : Parcelable, Serializable {
        var watched: Boolean = false
            set(value) {
                field = value
                watchedDate =
                    if (watched && watchedDate == null) Date() else if (!watched) null else watchedDate
            }
    }

    fun getFirstEpisodeUnwatched(serie: SerieResponse.Serie): Episode? {
        val idEp = this.episodesData.flatMap { it.value }.find { !it.watched }?.id ?: 0
        return if (idEp != 0) serie.getEpisode(idEp) else null
    }

    fun checkEpisodesFromSeason(seasonNumber:Int):Boolean {
        val itemList= this.episodesData.getValue("${seasonNumber}_")
        return itemList.all { it.watched }
    }

    fun areAllEpisodesWatched(): Boolean = this.episodesData.flatMap { it.value }.all { it.watched }

    fun isSeasonWatched(numSeason: Int): Boolean =
        episodesData.filterKeys { it == numSeason.toString() }[numSeason.toString()]?.all { it.watched }
            ?: false

    fun countEpisodesWatched(): Int = this.episodesData.flatMap { it.value }.count { it.watched }

    fun getProgress(serie: SerieResponse.Serie): Int {
        return countEpisodesWatched() * 100 / serie.numberOfEpisodes
    }

    fun watchEpisode() {

    }

    fun unWatchEpisode() {

    }


}

