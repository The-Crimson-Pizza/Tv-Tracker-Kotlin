package com.thecrimsonpizza.tvtrackerkotlin.core.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.util.*

class Util {
    fun getLanguageString(): String {
        return Locale.getDefault().toString().replace("_", "-")
    }
}

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities: NetworkCapabilities? =
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork);
    return capabilities != null &&
            (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
}


fun getImageNoPlaceholder(url: String?, image: ImageView, contexto: Context) {
    Glide.with(contexto)
        .load(url)
        .into(image)
}