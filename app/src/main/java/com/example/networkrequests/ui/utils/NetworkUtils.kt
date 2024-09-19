package com.example.networkrequests.ui.utils

import android.app.PendingIntent
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.net.Socket

object NetworkUtils {
    lateinit var connectivityManager: ConnectivityManager

    fun initManager(context: Context){
        connectivityManager = context.getSystemService( Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    }
   fun hasActiveNetwork ():Boolean {
       val activeNetwork = connectivityManager.activeNetwork //Returns the Active Network
       //Check whether the network is connected
      //connectivityManager.ge

       return activeNetwork!=null
   }
     class ConnectivityObserver( private val context: Context){
        private val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        fun observe(): Flow<ConnectivityStatus>{
         return callbackFlow {
             val callback = object : ConnectivityManager.NetworkCallback() {
                 override fun onAvailable(network: Network) {
                     super.onAvailable(network)
                     launch { send(ConnectivityStatus.AVAILABLE) }
                 }

                 override fun onLosing(network: Network, maxMsToLive: Int) {
                     super.onLosing(network, maxMsToLive)
                     launch { send(ConnectivityStatus.LOSING) }
                 }

                 override fun onLost(network: Network) {
                     super.onLost(network)
                     launch { send(ConnectivityStatus.LOST) }
                 }

                 override fun onUnavailable() {
                     super.onUnavailable()
                     launch { send(ConnectivityStatus.UNAVAILABLE) }
                 }
             }
             connectivityManager.registerDefaultNetworkCallback(callback)
             awaitClose {
                 connectivityManager.unregisterNetworkCallback(callback)
             }
         }.distinctUntilChanged()
        }
    }
    enum class ConnectivityStatus{
        AVAILABLE,UNAVAILABLE,LOSING,LOST
    }
}

/*In this short exercise we will work with  Connectivity Manager
One of the system tools
that we will help us  check everything about network connectivity
It also notifies applications about changes in network
The primary responsibilities of this class are to:

Monitor network connections (Wi-Fi, GPRS, UMTS, etc.)
Send broadcast intents when network connectivity changes
Attempt to "fail over" to another network when connectivity to a network is lost
Provide an API that allows applications to query the coarse-grained or fine-grained state of the available networks
Provide an API that allows applications to request and select networks for their data traffic

It is advisable to use wifi to do large network transfers  as mobile data is metered and expensive
We should also check the state of the network b4 carrying out any operation

Here we can for whether network types like wifi and mobile data are available

 */