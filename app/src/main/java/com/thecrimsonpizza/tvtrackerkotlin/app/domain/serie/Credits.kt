package com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Credits(@Expose val cast: List<Cast>? = null) : Serializable {

    class Cast(
        var id: Int,
        var name: String,
        var character: String?,
        var gender: Int,
        @SerializedName("profile_path") var profilePath: String?
    ) : Serializable
}