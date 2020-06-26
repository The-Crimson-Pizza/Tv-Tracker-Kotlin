package com.thecrimsonpizza.tvtrackerkotlin.app.data.remote

import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.GET_PEOPLE_API_EXTRAS
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.GET_SERIE_API_EXTRAS
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.MADRID
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.POP_DESC
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.TRAILER
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.YOUTUBE
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.BiFunction
import java.util.*

val request = ServiceBuilder.buildService(TmdbApi::class.java)
var language = Locale.getDefault().toString().replace("_", "-")

fun getTrendingSeries(): Observable<BasicResponse?>? {
    return request.getTrendingSeries()
}

fun getNewSeries(): Observable<BasicResponse?>? {
    return request.getNewSeries(2020, language, POP_DESC)
}

fun getSerie(idSerie: Int): Observable<SerieResponse.Serie>? {
    val obsSerie: Observable<SerieResponse.Serie> =
        request.getSerie(idSerie, language, GET_SERIE_API_EXTRAS)
    val obsVideo: Observable<VideosResponse> =
        request.getTrailer(idSerie)
    return Observable.zip<SerieResponse.Serie, VideosResponse, SerieResponse.Serie>(obsSerie,
        obsVideo,
        BiFunction<SerieResponse.Serie, VideosResponse, SerieResponse.Serie> { serie: SerieResponse.Serie, videosResponse: VideosResponse ->
            val trailers: List<Video> = videosResponse.results
            for (v in trailers) {
                if (v.getType().equals(TRAILER) && v.getSite().equals(YOUTUBE)) {
                    serie.video = v
                    break
                }
            }
            serie
        }
    )
}

fun getSeasons(
    idSerie: Int,
    firstSeason: Int,
    numSeasons: Int
): Single<List<Season>?>? {
    return Observable.range(firstSeason, numSeasons)
        .observeOn(AndroidSchedulers.mainThread())
        .flatMap<Any> { i: Int ->
            request.getSeason(
                idSerie,
                i,
                language
            )
        }
        .toList()
}

fun getPerson(idPerson: Int): Observable<PersonResponse.Person?>? {
    return request.getPerson(idPerson, language, GET_PEOPLE_API_EXTRAS)
}

fun searchPerson(query: String): Observable<PersonResponse?>? {
    return request.searchPerson(query, language)
}

fun searchSerie(query: String): Observable<SerieResponse?>? {
    return request.searchSerie(query, language)
}

fun getByGenre(idGenre: Int): Observable<BasicResponse?>? {
    return request.getSeriesByGenre(idGenre, language, MADRID, POP_DESC)
}

fun getByNetwork(idNetwork: Int): Observable<BasicResponse?>? {
    return request.getSeriesByNetwork(idNetwork, language, MADRID, POP_DESC)
}