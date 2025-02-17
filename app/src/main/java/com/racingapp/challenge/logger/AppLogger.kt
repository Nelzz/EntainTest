/* (C) 2025. All rights reserved. */
package com.racingapp.challenge.logger

import com.racingapp.domain.logger.Logger
import javax.inject.Inject
import timber.log.Timber

class AppLogger @Inject constructor() : Logger {
  override fun log(tag: String, message: String) {
    Timber.tag(tag).d(message)
  }

  override fun error(message: String) {
    Timber.e(message)
  }

  override fun info(message: String) {
    Timber.i(message)
  }
}
