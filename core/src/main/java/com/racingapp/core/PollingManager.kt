/* (C) 2025. All rights reserved. */
package com.racingapp.core

import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

class PollingManager
@Inject
constructor(
  private val coroutineScope: CoroutineScope,
  private val timeoutMillis: Long = TIMEOUT_IN_MILLIS,
) {

  private var job: Job? = null

  fun startPolling(intervalMillis: Long, fetchData: suspend () -> Unit) {
    stopPolling()

    job =
      coroutineScope.launch {
        while (isActive) {
          withTimeoutOrNull(timeoutMillis) { fetchData() }
          delay(intervalMillis)
        }
      }
  }

  fun stopPolling() {
    job?.cancel()
    job = null
  }

  companion object {
    const val TIMEOUT_IN_MILLIS = 3000L
    const val INTERVAL_IN_MILLIS = 5000L
  }
}
