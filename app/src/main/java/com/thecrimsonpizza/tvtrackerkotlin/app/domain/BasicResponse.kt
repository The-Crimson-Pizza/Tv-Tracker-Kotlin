package com.thecrimsonpizza.tvtrackerkotlin.app.domain

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

class BasicResponse(@SerializedName("results") @Expose var basicSeries: List<SerieBasic>) {

    @Parcelize
    class SerieBasic(
        val id: Int,
        val name: String,
        @SerializedName("poster_path") val posterPath: String?,
        @SerializedName("vote_average") var voteAverage: Float = 0.0f
    ) : Parcelable
}