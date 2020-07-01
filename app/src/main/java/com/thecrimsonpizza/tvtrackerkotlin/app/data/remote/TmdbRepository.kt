package com.thecrimsonpizza.tvtrackerkotlin.app.data.remote

import com.thecrimsonpizza.tvtrackerkotlin.app.domain.BasicResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.actor.PersonResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.VideoResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.BaseFragment
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.GET_PEOPLE_API_EXTRAS
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.GET_SERIE_API_EXTRAS
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.MADRID
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.POP_DESC
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.TRAILER
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.YOUTUBE
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.Util
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
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

    fun getSerie(idSerie: Int): Observable<SerieResponse.Serie> {
        val obsSerie: Observable<SerieResponse.Serie> =
            request.getSerie(idSerie, language, GET_SERIE_API_EXTRAS)
        val obsVideo: Observable<VideoResponse> =
            request.getTrailer(idSerie)
        return Observable.zip<SerieResponse.Serie, VideoResponse, SerieResponse.Serie>(obsSerie,
            obsVideo,
            BiFunction<SerieResponse.Serie, VideoResponse, SerieResponse.Serie> { serie: SerieResponse.Serie, videosResponse: VideoResponse ->
                val trailers: List<VideoResponse.Video> = videosResponse.results
                for (v in trailers) {
                    if (v.type.equals(TRAILER) && v.site.equals(YOUTUBE)) {
                        serie.video = v
                        break
                    }
                }
                serie
            }
        )
    }

    fun getSeasons(idSerie: Int, firstSeason: Int, numSeasons: Int): Single<MutableList<Any>> {
        return Observable.range(firstSeason, numSeasons)
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap<Any> { i: Int ->
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