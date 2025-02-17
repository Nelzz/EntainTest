/* (C) 2025. All rights reserved. */
package com.racingapp.challenge.di

import com.racingapp.challenge.logger.AppLogger
import com.racingapp.domain.logger.Logger
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppLoggerModule {

  @Binds @Singleton abstract fun bindLogger(logger: AppLogger): Logger
}
