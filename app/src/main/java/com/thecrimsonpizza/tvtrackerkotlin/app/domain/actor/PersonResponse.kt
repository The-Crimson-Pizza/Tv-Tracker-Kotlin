package com.thecrimsonpizza.tvtrackerkotlin.app.domain.actor

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PersonResponse: Serializable {

    @SerializedName("results")
    @Expose
    var results: List<Person>? = null

    fun PersonResponse(results: List<Person>?) {
        this.results = results
    }

    class Person(
        var id: Int,
        var name: String,
        var birthday: String?,
        var deathday: String?,
        var gender: Int,
        var biography: String?,
        var homepage: String?,
        @SerializedName("place_of_birth") var placeOfBirth: String?,
        @SerializedName("profile_path") var profilePath: String?,
        @SerializedName("known_for_department") var known: String?,
        @SerializedName("tv_credits") var tvCredits: TvCredits?,
        @SerializedName("movie_credits") var movieCredits: MovieCredits?,
        @SerializedName("external_ids") var externalIds: ExternalIds?
    ) : Serializable {

        val isDead: Boolean
            get() = deathday != null

        class ExternalIds(
            @SerializedName("instagram_id") var instagramId: String?,
            @SerializedName("twitter_id") var twitterId: String?,
            @SerializedName("imdb_id") var imdbId: String?
        ) : Serializable
    }
}