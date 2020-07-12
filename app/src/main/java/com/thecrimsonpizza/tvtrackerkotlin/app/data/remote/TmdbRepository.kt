package com.thecrimsonpizza.tvtrackerkotlin.app.data.remote

import com.thecrimsonpizza.tvtrackerkotlin.app.domain.BasicResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.actor.PersonResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.seasons.Season
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.VideoResponse
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.GET_PEOPLE_API_EXTRAS
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.GET_SERIE_API_EXTRAS
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.MADRID
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.POP_DESC
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.TRAILER
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.YOUTUBE
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.Util
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.BiFunction


object TmdbRepository {

    private val request = ServiceBuilder.buildService(TmdbApi::class.java)
    var language = Util().getLanguageString()


    fun getTrendingSeries(): Observable<BasicResponse> {
        return request.getTrendingSeries()
    }

    fun getNewSeries(): Observable<BasicResponse> {
        return request.getNewSeries(2020, language, POP_DESC)
    }

    private fun getSerie(idSerie: Int): Observable<SerieResponse.Serie> {
        val obsSerie: Observable<SerieResponse.Serie> =
            request.getSerie(idSerie, language, GET_SERIE_API_EXTRAS)
        val obsVideo: Observable<VideoResponse> = request.getTrailer(idSerie)
        return Observable.zip<SerieResponse.Serie, VideoResponse, SerieResponse.Serie>(
            obsSerie, obsVideo, BiFunction<SerieResponse.Serie, VideoResponse, SerieResponse.Serie>
            { serie: SerieResponse.Serie, videosResponse: VideoResponse ->
                val trailers: List<VideoResponse.Video> = videosResponse.results
                for (v in trailers) {
                    if (v.type == TRAILER && v.site == YOUTUBE) {
                        serie.video = v
                        break
                    }
                }
                serie
            }
        )
    }

    fun getSerieWithSeasons(id: Int): Observable<SerieResponse.Serie> {
        return getSerie(id)
            .flatMap { serie ->
                getSeasons(id, serie.numberOfSeasons).map {
                    serie.withSeasons(it, serie)
                }.toObservable()
            }
    }

    private fun getSeasons(idSerie: Int, numSeasons: Int): Single<MutableList<Season>> {
        return Observable.range(1, numSeasons)
            .flatMap { i: Int ->
                request.getSeason(idSerie, i, language)
            }.toList()
    }

    fun getPerson(idPerson: Int): Observable<PersonResponse.Person> {
        return request.getPerson(idPerson, language, GET_PEOPLE_API_EXTRAS)
    }

    fun searchPerson(query: String): Observable<PersonResponse> {
        return request.searchPerson(query, language)
    }

    fun searchSerie(query: String): Observable<SerieResponse> {
        return request.searchSerie(query, language)
    }

    fun getByGenre(idGenre: Int): Observable<BasicResponse> {
        return request.getSeriesByGenre(idGenre, language, MADRID, POP_DESC)
    }

    fun getByNetwork(idNetwork: Int): Observable<BasicResponse> {
        return request.getSeriesByNetwork(idNetwork, language, MADRID, POP_DESC)
    }

}