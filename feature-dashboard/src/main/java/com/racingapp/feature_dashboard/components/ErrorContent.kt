/* (C) 2025. All rights reserved. */
package com.racingapp.feature_dashboard.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.racingapp.feature_dashboard.R
import com.racingapp.ui_common.UiState
import com.racingapp.utils.NetworkException
import com.racingapp.utils.NetworkExceptionType

@Composable
fun ErrorContent(state: UiState.Error, content: suspend (String) -> Unit) {
  ErrorMessage(state.exception as Exception) { LaunchedEffect(Unit) { content(it) } }
  LoadingContent()
}

@Composable
private fun ErrorMessage(error: java.lang.Exception, content: @Composable (String) -> Unit) {
  val context = LocalContext.current
  val message =
    when (error) {
      is NetworkException -> {
        when (val type = error.type) {
          is NetworkExceptionType.NoInternet -> context.getString(R.string.error_no_internet)
          is NetworkExceptionType.Timeout -> context.getString(R.string.error_timeout)
          is NetworkExceptionType.ServerError -> context.getString(R.string.error_server, type.code)
          is NetworkExceptionType.Cancelled -> context.getString(R.string.error_unknown)
          is NetworkExceptionType.UnknownHost -> context.getString(R.string.error_no_internet)
          else -> context.getString(R.string.error_unknown)
        }
      }
      else -> {
        context.getString(R.string.error_unknown)
      }
    }

  content(message)
}
