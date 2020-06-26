package com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Similar : Serializable {
    @Expose
    var results: List<Result>? = null

    class Result(
        var id: Int,
        var name: String,
        var popularity: Float,
        @SerializedName("original_name") var originalName: String?
    ) : Serializable
}
