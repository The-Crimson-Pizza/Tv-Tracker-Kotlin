package com.thecrimsonpizza.tvtrackerkotlin.core.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import java.util.*

class Util {
    fun getLanguageString(): String {
        return Locale.getDefault().toString().replace("_", "-")
    }
}

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    var capabilities: NetworkCapabilities? = null
    if (connectivityManager != null)
        capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork);
    return capabilities != null &&
            (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
}