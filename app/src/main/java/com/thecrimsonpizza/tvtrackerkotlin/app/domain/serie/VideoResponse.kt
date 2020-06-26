package com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie

import com.google.gson.annotations.Expose
import java.io.Serializable

class VideosResponse(@Expose val results: List<Video>) : Serializable {

    class Video(
        val key: String,
        val site: String?,
        val type: String?
    ):        Serializable
}
