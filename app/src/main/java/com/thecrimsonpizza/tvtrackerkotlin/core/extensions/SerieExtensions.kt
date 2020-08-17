package com.thecrimsonpizza.tvtrackerkotlin.core.extensions

import com.thecrimsonpizza.tvtrackerkotlin.app.domain.seasons.Episode
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.seasons.Season
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.FollowingSerie
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse


fun List<Season>.sort(): List<Season> {
    return this.sortedWith(nullsLast(compareBy { it.seasonNumber }))
}


//fun List<SerieResponse.Serie>.saveToFirebase() {
//    FirebaseDatabaseRealtime(FirebaseAuth.getInstance().currentUser).setSeriesFav(this)
//}


fun SerieResponse.Serie.getLastUnwatched(dataList: List<FollowingSerie>): Episode? {
    val data = this.followingData
    val id = data?.episodesData?.flatMap { it.value }?.firstOrNull { !it.watched }?.id
    return this.seasons.flatMap { it.episodes }.first { it.id == id }

//    for (s in this.seasons) {
//        for (e in s.episodes) {
//            if (!e.followingData.watched) return e
//        }
//    }
//    return null
}
