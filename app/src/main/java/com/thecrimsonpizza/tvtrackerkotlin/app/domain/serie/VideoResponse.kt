package com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie

import com.google.gson.annotations.Expose
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants
import java.io.Serializable

class VideoResponse(
    @Expose val results: List<Video> = listOf()
) : Serializable {

    fun getTrailer():Video{
        return this.results.first { it.type == GlobalConstants.TRAILER && it.site == GlobalConstants.YOUTUBE }
    }

    data class Video(
        val key: String = "",
        val site: String = "",
        val type: String = ""
    ) : Serializable
}
