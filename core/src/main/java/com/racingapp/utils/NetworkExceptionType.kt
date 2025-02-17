/* (C) 2025. All rights reserved. */
package com.racingapp.utils

sealed class NetworkExceptionType {
  data object NoInternet : NetworkExceptionType()

  data class ServerError(val code: Int) : NetworkExceptionType()

  data object Timeout : NetworkExceptionType()

  data object Cancelled : NetworkExceptionType()

  data object UnknownHost : NetworkExceptionType()

  data object ConnectionError : NetworkExceptionType()
}

data class NetworkException(val type: NetworkExceptionType) : Exception()
