/* (C) 2025. All rights reserved. */
package com.racingapp.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn

@Singleton
class NetworkMonitor
@Inject
constructor(
  @ApplicationContext private val context: Context,
) {

  private val connectivityManager by lazy {
    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
  }

  fun observeNetwork(): Flow<Boolean> =
    callbackFlow {
        val networkCallback =
          object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
              trySend(true)
            }

            override fun onLost(network: Network) {
              trySend(false)
            }
          }

        connectivityManager.registerDefaultNetworkCallback(networkCallback)

        trySend(isConnected)

        awaitClose { connectivityManager.unregisterNetworkCallback(networkCallback) }
      }
      .distinctUntilChanged()
      .stateIn(
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
        started = SharingStarted.WhileSubscribed(STOPPING_TIME),
        initialValue = true)

  val isConnected: Boolean
    get() {
      val network = connectivityManager.activeNetwork
      val capabilities = network?.let { connectivityManager.getNetworkCapabilities(it) }
      return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
    }

  companion object {
    private const val STOPPING_TIME = 5000L
  }
}
