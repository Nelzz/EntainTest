/* (C) 2025. All rights reserved. */
package com.racingapp.domain.usecases

import com.racingapp.domain.repositories.Repository

class PollingUseCase(private val repository: Repository) {

  fun startPolling(interval: Long) = repository.startPolling(interval)

  fun stopPolling() = repository.stopPolling()
}
