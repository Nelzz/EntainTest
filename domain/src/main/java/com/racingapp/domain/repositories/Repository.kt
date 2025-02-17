/* (C) 2025. All rights reserved. */
package com.racingapp.domain.repositories

import com.racingapp.domain.models.RaceDetails
import kotlinx.coroutines.flow.Flow

interface Repository {
  suspend fun getUpcomingRaces()

  val races: Flow<Result<List<RaceDetails>>>

  fun startPolling(intervalMillis: Long)

  fun stopPolling()
}
