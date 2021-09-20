package com.olaelectric.mfg.base.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import com.olaelectric.mfg.base.listeners.NetWorkListener
import com.olaelectric.mfg.logging.AppLogger

object NetWorkUtil {

    val TAG = NetWorkUtil::class.simpleName.toString()

    fun onNetWorkChange(context: Context, netWorkListener: NetWorkListener) {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(object :
                ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    AppLogger.e(TAG, "The default network is now: $network")
                    netWorkListener.onConnectionAvailable()
                }

                override fun onLost(network: Network) {
                    AppLogger.e(
                        TAG,
                        "The application no longer has a default network. The last default network was $network"
                    )
                    netWorkListener.onConnectionLost()
                }
            })
    }
}
