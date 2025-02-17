/* (C) 2025. All rights reserved. */
package com.racingapp.di

import com.racingapp.utils.DefaultDispatcherProvider
import com.racingapp.utils.DispatcherProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DispatcherModule {

  @Binds abstract fun bindDispatcherProvider(impl: DefaultDispatcherProvider): DispatcherProvider
}
