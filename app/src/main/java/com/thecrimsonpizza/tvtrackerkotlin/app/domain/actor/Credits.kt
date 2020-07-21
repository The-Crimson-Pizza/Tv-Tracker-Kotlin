package com.thecrimsonpizza.tvtrackerkotlin.app.domain.actor

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Credits(@Expose var cast: List<Cast> = mutableListOf()) : Serializable {

    data class Cast(
        var id: Int = 0,
        var name: String = "",
        var character: String = "",

        @SerializedName("episode_count") var episodeCount: Int = 0,
        @SerializedName("poster_path") var posterPath: String? = null,
        @SerializedName("first_air_date") var firstAirDate: String = "",
        @SerializedName("vote_average") var voteAverage: Float = 0f,

        @SerializedName("profile_path") var profilePath: String? = null,

        var title: String = "",
        @SerializedName("original_title") var originalTitle: String = "",
        @SerializedName("release_date") var releaseDate: String = ""

    ) : Serializable, Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString().toString(),
            parcel.readFloat(),
            parcel.readString(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString()
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeInt(id)
            parcel.writeString(name)
            parcel.writeString(character)
            parcel.writeInt(episodeCount)
            parcel.writeString(posterPath)
            parcel.writeString(firstAirDate)
            parcel.writeFloat(voteAverage)
            parcel.writeString(profilePath)
            parcel.writeString(title)
            parcel.writeString(originalTitle)
            parcel.writeString(releaseDate)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Cast> {
            override fun createFromParcel(parcel: Parcel): Cast {
                return Cast(parcel)
            }

            override fun newArray(size: Int): Array<Cast?> {
                return arrayOfNulls(size)
            }
        }
    }
}