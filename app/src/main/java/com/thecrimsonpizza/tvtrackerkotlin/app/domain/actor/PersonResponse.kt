package com.thecrimsonpizza.tvtrackerkotlin.app.domain.actor

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.thecrimsonpizza.tvtrackerkotlin.core.base.BaseClass
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

class PersonResponse : Serializable {

    @SerializedName("results")
    @Expose
    lateinit var results: List<Person>

    @Parcelize
    class Person(
        override var id: Int,
        override var name: String,
        var birthday: String?,
        var deathday: String?,
        var gender: Int,
        var biography: String?,
        var homepage: String?,
        @SerializedName("place_of_birth") var placeOfBirth: String?,
        @SerializedName("profile_path") override var posterPath: String? = "",
        @SerializedName("known_for_department") val known: String,
        @SerializedName("tv_credits") val tvCredits: Credits?,
        @SerializedName("movie_credits") val movieCredits: Credits?,
        @SerializedName("external_ids") val externalIds: ExternalIds?
    ) : BaseClass {

        val isDead: Boolean
            get() = deathday != null

        class ExternalIds(
            @SerializedName("instagram_id") var instagramId: String?,
            @SerializedName("twitter_id") var twitterId: String?,
            @SerializedName("imdb_id") var imdbId: String?
        ) : Serializable
    }
}