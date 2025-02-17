/* (C) 2025. All rights reserved. */
package com.racingapp.domain.usecases

import com.racingapp.domain.models.RaceDetails
import com.racingapp.domain.repositories.Repository
import java.time.Duration
import java.time.Instant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RetrieveUseCase(
  private val repository: Repository,
) {
  fun getRaces(threshold: Int = DEFAULT_THRESHOLD_IN_SECONDS): Flow<Result<List<RaceDetails>>> {
    return repository.races.map { result ->
      result.map { races ->
        races
          .filter {
            val now = Instant.now()
            val raceStartTime = Instant.ofEpochMilli(it.advertisedStart.seconds)
            val timeDifference = Duration.between(now, raceStartTime).seconds
            timeDifference >= threshold
          }
          .sortedBy { it.advertisedStart.seconds }
      }
    }
  }

  suspend fun manualFetch() {
    repository.getUpcomingRaces()
  }

  companion object {
    private const val DEFAULT_THRESHOLD_IN_SECONDS = -60
  }
}
