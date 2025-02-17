/* (C) 2025. All rights reserved. */
package com.racingapp.ui_common

sealed class UiState<out T> {
  data object Loading : UiState<Nothing>()

  data class Success<T>(val data: T) : UiState<T>()

  data class Error(val exception: Throwable) : UiState<Nothing>()
}
