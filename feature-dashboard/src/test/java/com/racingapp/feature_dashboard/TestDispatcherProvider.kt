/* (C) 2025. All rights reserved. */
package com.racingapp.feature_dashboard

import com.racingapp.utils.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher

class TestDispatcherProvider : DispatcherProvider {
  override val main: CoroutineDispatcher = StandardTestDispatcher()
  override val io: CoroutineDispatcher = StandardTestDispatcher()
  override val default: CoroutineDispatcher = StandardTestDispatcher()
}
