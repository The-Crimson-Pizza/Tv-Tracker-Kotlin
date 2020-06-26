package com.thecrimsonpizza.tvtrackerkotlin.app.data.remote

import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interface with the TMDB API calls
 */
interface TmdbApi {

    @Headers("Accept: application/json")
    @GET("trending/tv/day")
    fun getTrendingSeries(): Observable<BasicResponse>

    @GET("discover/tv")
    fun getNewSeries(
        @Query("first_air_date_year") year: Int,
        @Query("language") language: String,
        @Query("sort_by") sort: String
    ): Observable<BasicResponse>

    @GET("tv/{id_serie}")
    fun getSerie(
        @Path("id_serie") id: Int,
        @Query("language") language: String,
        @Query("append_to_response") append: String
    ): Observable<SerieResponse.Serie>

    @GET("tv/{tv_id}/videos")
    fun getTrailer(@Path("tv_id") idSerie: Int): Observable<VideosResponse>

    @GET("tv/{id_serie}/season/{season_number}")
    fun getSeason(
        @Path("id_serie") id: Int,
        @Path("season_number") season: Int,
        @Query("language") language: String
    ): Observable<Season>

    @GET("person/{person_id}")
    fun getPerson(
        @Path("person_id") idPersona: Int,
        @Query("language") language: String,
        @Query("append_to_response") append: String
    ): Observable<PersonResponse.Person>

    @GET("search/person")
    fun searchPerson(
        @Query("query") query: String,
        @Query("language") language: String
    ): Observable<PersonResponse>

    @GET("search/tv")
    fun searchSerie(
        @Query("query") query: String,
        @Query("language") language: String
    ): Observable<SerieResponse>

    @GET("discover/tv")
    fun getSeriesByGenre(
        @Query("with_genres") idGenre: Int,
        @Query("language") language: String,
        @Query("timezone") zone: String,
        @Query("sort_by") sort: String
    ): Observable<BasicResponse>

    @GET("discover/tv")
    fun getSeriesByNetwork(
        @Query("with_networks") idNetwork: Int,
        @Query("language") language: String,
        @Query("timezone") zone: String,
        @Query("sort_by") sort: String
    ): Observable<BasicResponse>
}