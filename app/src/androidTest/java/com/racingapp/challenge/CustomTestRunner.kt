/* (C) 2025. All rights reserved. */
package com.racingapp.challenge

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.racingapp.challenge.di.AppModule
import com.racingapp.domain.repositories.Repository
import com.racingapp.domain.usecases.PollingUseCase
import com.racingapp.domain.usecases.RetrieveUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

class CustomTestRunner : AndroidJUnitRunner() {
  override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
    return super.newApplication(cl, HiltTestApplication::class.java.name, context)
  }
}

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [AppModule::class])
object TestAppModule {

  @Provides
  @Singleton
  fun provideRetrieveUseCase(repository: Repository): RetrieveUseCase {
    return RetrieveUseCase(repository)
  }

  @Provides
  @Singleton
  fun providePollingUseCase(repository: Repository): PollingUseCase {
    return PollingUseCase(repository)
  }
}
