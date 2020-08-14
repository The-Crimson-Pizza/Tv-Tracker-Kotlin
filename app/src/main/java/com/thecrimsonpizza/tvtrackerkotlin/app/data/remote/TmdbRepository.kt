package com.thecrimsonpizza.tvtrackerkotlin.app.data.remote

import androidx.lifecycle.liveData
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.actor.PersonResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.seasons.Season
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.BasicResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.sort
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.GET_PEOPLE_API_EXTRAS
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.GET_SERIE_API_EXTRAS
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.MADRID
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.POP_DESC
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.Resource
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.Util
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import java.time.Year


object TmdbRepository {

    private val request = ServiceBuilder.apiService
    var language = Util().getLanguageString()

    fun getTrendingSeries() = liveData(IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = request.getTrendingSeries()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error"))
        }
    }

    fun getNewSeries() = liveData(IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = request.getNewSeries(Year.now().value, language, POP_DESC)
                )
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error"))
        }
    }

    suspend fun getSerie(idSerie: Int): SerieResponse.Serie {
        val trailerRequest = GlobalScope.async {
            request.getTrailer(idSerie)
        }
        val serieRequest = GlobalScope.async {
            request.getSerie(idSerie, language, GET_SERIE_API_EXTRAS)
        }

        val serie = serieRequest.await()
        val trailerResults = trailerRequest.await()

        val seasons = (1..(serie.numberOfSeasons)).map { i: Int ->
            GlobalScope.async {
                request.getSeason(idSerie, i, language)
            }
        }.awaitAll()

//        val seasons = videoRequest.await()

        serie.seasons = seasons.sort()
        if (trailerResults.results.isNotEmpty()) serie.video = trailerResults.getTrailer()
        return serie
    }

    private suspend fun getSeasons(idSerie: Int, numSeasons: Int): List<Season> {
        return (1..numSeasons).map { i: Int ->
            GlobalScope.async {
                request.getSeason(idSerie, i, language)
            }
        }.awaitAll()
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

    fun getByGenre(idGenre: Int, page: Int = 1): Observable<BasicResponse> {
        return request.getSeriesByGenre(idGenre, page, language, MADRID, POP_DESC)
    }

    fun getByNetwork(idNetwork: Int): Observable<BasicResponse> {
        return request.getSeriesByNetwork(idNetwork, language, MADRID, POP_DESC)
    }

}


//    private fun getSerie(idSerie: Int) = liveData(IO){
//        val obsSerie: Observable<SerieResponse.Serie> =
//            request.getSerie(idSerie, language, GET_SERIE_API_EXTRAS)
//        val obsVideo: Observable<VideoResponse> = request.getTrailer(idSerie)
//        return Observable.zip<SerieResponse.Serie, VideoResponse, SerieResponse.Serie>(
//            obsSerie, obsVideo, BiFunction<SerieResponse.Serie, VideoResponse, SerieResponse.Serie>
//            { serie: SerieResponse.Serie, videosResponse: VideoResponse ->
//                val trailers: List<VideoResponse.Video> = videosResponse.results
//                for (v in trailers) {
//                    if (v.type == TRAILER && v.site == YOUTUBE) {
//                        serie.video = v
//                        break
//                    }
//                }
//                serie
//            }
//        )