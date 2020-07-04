package com.thecrimsonpizza.tvtrackerkotlin.core.extensions

import com.google.firebase.auth.FirebaseAuth
import com.thecrimsonpizza.tvtrackerkotlin.app.data.local.FirebaseDatabaseRealtime
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.seasons.Episode
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.seasons.Season
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse
import java.util.*


fun List<Season>.sort(){
    this.sortedWith(nullsLast(compareBy { it.seasonNumber }))
}

fun List<SerieResponse.Serie>.setLastWatched() {
    for (tvShow in this)
        tvShow.lastEpisodeWatched = tvShow.seasons
            .flatMap { it.episodes }
            .filter { it.watchedDate != null }
            .maxBy { it.watchedDate.toString() }
}

fun List<SerieResponse.Serie>.saveToFirebase() {
    FirebaseDatabaseRealtime(FirebaseAuth.getInstance().currentUser).setSeriesFav(this)
}


fun SerieResponse.Serie.countEpisodesWatched(): Int {
    return this.seasons.flatMap { it.episodes }.count { it.watched }
}

fun SerieResponse.Serie.getLastUnwatched():Episode? {
    this.seasons.sort()
    for (s in this.seasons) {
        for (e in s.episodes) {
            if (!e.watched) return e
        }
    }
    return null
}

fun SerieResponse.Serie.getProgress(): Int {
    return this.countEpisodesWatched() * 100 / this.numberOfEpisodes
}

fun SerieResponse.Serie.checkSeasonsFinished(): Boolean =
    this.seasons.all { it.watched }

fun SerieResponse.Serie.checkEpisodesFinished(): Boolean =
    this.seasons.flatMap { it.episodes }.all { it.watched }

fun SerieResponse.Serie.checkEpisodesBySeasonFinished(): Boolean =
    this.seasons.flatMap { it.episodes }.all { it.watched }

fun SerieResponse.Serie.watchEpisode(
    list: List<SerieResponse.Serie>,
    isWatched: Boolean
) {
    val episode: Episode? = this.getLastUnwatched()
    list.filter { it.id == this.id }.flatMap { it.seasons }
        .first { it.id == episode?.id }.watched = isWatched
    list.filter { it.id == this.id }.flatMap { it.seasons }
        .first { it.id == episode?.id }.watchedDate = Date()

    list.filter { it.id == this.id }.flatMap { it.seasons }
        .forEach { it.watched = list[list.indexOf(this)].checkEpisodesFinished() }
    list.filter { it.id == this.id }
        .forEach { it.finished = list[list.indexOf(this)].checkSeasonsFinished() }

    list.saveToFirebase()
}