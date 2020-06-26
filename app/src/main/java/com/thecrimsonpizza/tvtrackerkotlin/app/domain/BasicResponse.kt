package com.thecrimsonpizza.tvtrackerkotlin.app.domain

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BasicResponse {

    @SerializedName("results")
    @Expose
    var basicSeries: List<SerieBasic>? = null

    fun BasicResponse(basicSeries: List<SerieBasic>?) {
        this.basicSeries = basicSeries
    }

    class SerieBasic(
        val id: Int,
        val name: String?,
        @SerializedName("poster_path") val posterPath: String?,
        @SerializedName("vote_average") var voteAverage: Float = 0f
    ) : Parcelable {

        fun isFav(followingList: List<SerieBasic>): Boolean {
            for (s in followingList) {
                if (id == s.id) {
                    return true
                }
            }
            return false
        }

        override fun writeToParcel(dest: Parcel?, flags: Int) {
            dest?.let {
                dest.writeString(name)
                dest.writeString(posterPath)
                dest.writeInt(id)
            }
        }

        override fun describeContents(): Int = 0

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<SerieBasic> = object : Parcelable.Creator<SerieBasic> {
                override fun newArray(size: Int): Array<SerieBasic?> = arrayOfNulls(size)
                override fun createFromParcel(source: Parcel): SerieBasic = SerieBasic(source)
            }
        }

        constructor(source: Parcel) : this(
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readFloat()
        )
    }
}