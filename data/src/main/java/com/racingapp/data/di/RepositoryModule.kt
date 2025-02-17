/* (C) 2025. All rights reserved. */
package com.racingapp.data.di

import com.racingapp.core.PollingManager
import com.racingapp.data.mappers.RaceSummaryMapper
import com.racingapp.data.repositories.RemoteRepository
import com.racingapp.data.services.RaceApiService
import com.racingapp.domain.logger.Logger
import com.racingapp.domain.repositories.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

  @Provides
  @Singleton
  fun provideRepository(
    api: RaceApiService,
    mapper: RaceSummaryMapper,
    pollingManager: PollingManager,
    logger: Logger
  ): Repository = RemoteRepository(api, mapper, pollingManager, logger)
}
