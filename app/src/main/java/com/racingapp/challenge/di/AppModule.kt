/* (C) 2025. All rights reserved. */
package com.racingapp.challenge.di

import com.racingapp.domain.repositories.Repository
import com.racingapp.domain.usecases.PollingUseCase
import com.racingapp.domain.usecases.RetrieveUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class AppModule {

  @Provides
  fun provideRetrieveUseCase(raceRepository: Repository): RetrieveUseCase {
    return RetrieveUseCase(raceRepository)
  }

  @Provides
  fun providePollingUseCase(raceRepository: Repository): PollingUseCase {
    return PollingUseCase(raceRepository)
  }
}
