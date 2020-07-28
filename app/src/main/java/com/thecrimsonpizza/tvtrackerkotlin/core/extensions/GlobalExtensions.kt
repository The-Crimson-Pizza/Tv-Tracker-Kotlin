package com.thecrimsonpizza.tvtrackerkotlin.core.extensions

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.FORMAT_DEFAULT
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.FORMAT_MINUTES
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Observable
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


fun String?.checkNull(context: Context): String? {
    return if (!this.isNullOrEmpty()) this
    else context.getString(R.string.no_data)
}

fun String.changeDateFormat(format: String): String {
    if(this == null || this.isEmpty()) Log.e("TAG", "NULO")
    val localDate = LocalDate.parse(this, DateTimeFormatter.ofPattern(FORMAT_DEFAULT))
    val formatter =
        DateTimeFormatter.ofPattern(format)
    val formattedString: String = localDate.format(formatter)
    return formattedString
//    return SimpleDateFormat(format, Locale.getDefault())        .format(LocalDate.parse(this, format))
}

fun String.parseToDate(): Date? {
    return Date.from(
        LocalDate.parse(this, DateTimeFormatter.ISO_DATE)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
    )
}

fun Int.getMinutes(): String {
    return String.format(Locale.getDefault(), FORMAT_MINUTES, this, 0)
}

fun String?.parseToLocalDate(): LocalDate {
    var formatter =
        DateTimeFormatter.ofPattern(FORMAT_DEFAULT)
    formatter = formatter.withLocale(Locale.getDefault())
    return LocalDate.parse(this, formatter)
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

fun <T> Observable<T>.toLiveData(): LiveData<T> {
    return LiveDataReactiveStreams.fromPublisher(this.toFlowable(BackpressureStrategy.LATEST))
}

fun <T : Any?> MutableLiveData<T>.withDefault(initialValue: T) = apply { setValue(initialValue) }