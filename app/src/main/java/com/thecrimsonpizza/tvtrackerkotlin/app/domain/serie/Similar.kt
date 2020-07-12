package com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Similar(@Expose var results: MutableList<Result> = mutableListOf()) : Serializable {

    data class Result(
        var id: Int = 0,
        var name: String = "",
        var popularity: Float = 0f,
        @SerializedName("original_name") var originalName: String = ""
    ) : Serializable
}
