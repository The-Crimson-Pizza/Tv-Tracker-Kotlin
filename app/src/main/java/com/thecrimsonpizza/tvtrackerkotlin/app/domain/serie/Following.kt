package com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie

import java.util.*

data class Following(
    var added: Boolean = false,
    var addedDate: Date? = null,
    var watchedDate: Date? = null
) {
    var watched: Boolean = false
        set(value) {
            field = value
            watchedDate =
                if (watched && watchedDate == null) Date() else if (!watched) null else watchedDate
        }
}