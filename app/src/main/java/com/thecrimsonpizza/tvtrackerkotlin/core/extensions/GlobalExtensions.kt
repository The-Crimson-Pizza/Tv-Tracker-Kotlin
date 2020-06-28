package com.thecrimsonpizza.tvtrackerkotlin.core.extensions

import android.content.Context
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.FORMAT_DEFAULT
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

fun String?.checkNull(context: Context): String? {
    return if (!this.isNullOrEmpty()) this
    else context.getString(R.string.no_data)
}

fun String.changeDateFormat(format: String?): String? {
    return SimpleDateFormat(FORMAT_DEFAULT, Locale.getDefault())
        .format(LocalDate.parse(this, DateTimeFormatter.ISO_DATE))
}

fun Date?.convertToString(pattern: String): String {
    return if (this != null) SimpleDateFormat(pattern, Locale.getDefault())
        .format(this)
    else return "No data"
}


fun String.translateStatus(): String {
    return when (this) {
        "Returning Series" -> "En emisión"
        "Planned" -> "Planeada"
        "In Production" -> "En producción"
        "Ended" -> "Terminada"
        "Canceled" -> "Cancelada"
        "Pilot" -> "Piloto"
        else -> "Sin datos"
    }
}

