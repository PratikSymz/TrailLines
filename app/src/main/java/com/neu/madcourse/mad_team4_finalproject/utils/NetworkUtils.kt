package com.neu.madcourse.mad_team4_finalproject.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class NetworkUtils(context: Context) {
    /* The Log tag */
    private val LOG_TAG = NetworkUtils::class.java.simpleName

    /* The Activity context using the utilities */
    private val mContext: Context = context

    /* Helper method to check whether the user has an active internet connection */
    fun isConnected(): Boolean {
        // Get the connectivity manager reference
        val connectivityManager =
            mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Get the network capabilities reference
        val networkCapabilities = connectivityManager
            .getNetworkCapabilities(connectivityManager.activeNetwork)

        return networkCapabilities != null && isOnline(networkCapabilities)
    }

    /* Helper method to check whether the user has active network adapters */
    private fun isOnline(networkCapabilities: NetworkCapabilities): Boolean {
        return (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
    }
}