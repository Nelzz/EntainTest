/* (C) 2025. All rights reserved. */
package com.racingapp.di

import com.racingapp.core.PollingManager
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object PollingScopeModule {

  @Provides
  @Reusable
  fun providePollingManager(): PollingManager {
    return PollingManager(CoroutineScope(Dispatchers.IO), PollingManager.TIMEOUT_IN_MILLIS)
  }
}
