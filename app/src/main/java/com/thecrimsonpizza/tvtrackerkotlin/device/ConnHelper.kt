package com.thecrimsonpizza.tvtrackerkotlin.device

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import androidx.lifecycle.LiveData

class ConnHelper  constructor(private val cm: ConnectivityManager) : LiveData<Boolean>() {

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network?) = postValue(true)
        override fun onLost(network: Network?) = postValue(false)
    }

    override fun onActive() {
        super.onActive()

        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        postValue(activeNetwork?.isConnectedOrConnecting == true)

        cm.registerDefaultNetworkCallback(networkCallback)
    }

    override fun onInactive() {
        super.onInactive()
        cm.unregisterNetworkCallback(networkCallback)
    }
}