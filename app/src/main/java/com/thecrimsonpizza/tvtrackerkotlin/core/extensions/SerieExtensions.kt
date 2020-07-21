package com.thecrimsonpizza.tvtrackerkotlin.core.extensions

import com.google.firebase.auth.FirebaseAuth
import com.thecrimsonpizza.tvtrackerkotlin.app.data.local.FirebaseDatabaseRealtime
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.BasicResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.actor.Credits
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.seasons.Episode
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.seasons.Season
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse
import java.util.*


fun List<Season>.sort() {
    this.sortedWith(nullsLast(compareBy { it.seasonNumber }))
}

fun List<SerieResponse.Serie>.setLastWatched() {
    for (tvShow in this)
        tvShow.lastEpisodeWatched = tvShow.seasons
            .flatMap { it.episodes }
            .filter { it.followingData.watchedDate != null }
            .maxBy { it.followingData.watchedDate.toString() }!!
}

fun List<SerieResponse.Serie>.saveToFirebase() {
    FirebaseDatabaseRealtime(FirebaseAuth.getInstance().currentUser).setSeriesFav(this)
}

fun SerieResponse.Serie.countEpisodesWatched(): Int {
    return this.seasons.flatMap { it.episodes }.count { it.followingData.watched }
}

fun SerieResponse.Serie.getLastUnwatched(): Episode? {
    this.seasons.sort()
    for (s in this.seasons) {
        for (e in s.episodes) {
            if (!e.followingData.watched) return e
        }
    }
    return null
}

fun SerieResponse.Serie.toBasic(): BasicResponse.SerieBasic{
    return BasicResponse.SerieBasic(this.id, this.name, this.posterPath)
}
fun Credits.Cast.toBasic(): BasicResponse.SerieBasic{
    return BasicResponse.SerieBasic(this.id, this.name, this.posterPath)
}

fun SerieResponse.Serie.getProgress(): Int {
    return this.countEpisodesWatched() * 100 / this.numberOfEpisodes
}

fun SerieResponse.Serie.checkSeasonsFinished(): Boolean =
    this.seasons.all { it.followingData.watched }

fun SerieResponse.Serie.checkEpisodesFinished(): Boolean =
    this.seasons.flatMap { it.episodes }.all { it.followingData.watched }

fun SerieResponse.Serie.checkEpisodesInSeasonFinished(pos: Int): Boolean =
    this.seasons.filter { it.id == this.seasons[pos].id }.flatMap { it.episodes }
        .all { it.followingData.watched }

fun SerieResponse.Serie.watchEpisode(
    list: List<SerieResponse.Serie>,
    isWatched: Boolean
) {
    val episode: Episode? = this.getLastUnwatched()
    list.filter { it.id == this.id }.flatMap { it.seasons }
        .first { it.id == episode?.id }.followingData.watched = isWatched
    list.filter { it.id == this.id }.flatMap { it.seasons }
        .first { it.id == episode?.id }.followingData.watchedDate = Date()

    list.filter { it.id == this.id }.flatMap { it.seasons }
        .forEach { it.followingData.watched = list[list.indexOf(this)].checkEpisodesFinished() }
    list.filter { it.id == this.id }
        .forEach { it.followingData.watched = list[list.indexOf(this)].checkSeasonsFinished() }

    list.saveToFirebase()
}

fun Episode.markAsWatched(watched: Boolean = true) {
    this.followingData.watched = watched
    this.followingData.watchedDate = if (watched) Date() else null
}

fun Season.markAsWatched(watched: Boolean = true) {
    this.followingData.watched = watched
    this.followingData.watchedDate = if (watched) Date() else null

    this.episodes.forEach {
        it.followingData.watched = watched
        it.followingData.watchedDate = if (watched) Date() else null
    }
}

fun SerieResponse.Serie.markAsWatched(watched: Boolean = true) {
    this.followingData.watched = watched
    this.followingData.watchedDate = if (watched) Date() else null
}


