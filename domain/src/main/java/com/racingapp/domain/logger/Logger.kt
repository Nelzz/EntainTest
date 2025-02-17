/* (C) 2025. All rights reserved. */
package com.racingapp.domain.logger

interface Logger {
  fun log(tag: String, message: String)

  fun error(message: String)

  fun info(message: String)
}
