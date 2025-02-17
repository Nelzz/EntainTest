/* (C) 2025. All rights reserved. */
package com.racingapp.feature_dashboard.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.racingapp.feature_dashboard.R
import com.racingapp.ui_common.components.AppText
import com.racingapp.ui_common.theme.LocalCustomColors
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.delay

private const val SECONDS_IN_MILLIS = 1000
private const val WARNING_THRESHOLD = 30
private const val COUNT_THRESHOLD_MINUTES = 5

@Composable
fun CountdownTimer(advertisedStartSeconds: Long) {
  val remainingTime by
    produceState(
      getRemainingTime(advertisedStartSeconds / SECONDS_IN_MILLIS), advertisedStartSeconds) {
        while (true) {
          delay(SECONDS_IN_MILLIS.toLong())
          value = getRemainingTime(advertisedStartSeconds / SECONDS_IN_MILLIS)
        }
      }

  Column {
    AppText(
      text =
        if (remainingTime > 0) {
          formatTime(remainingTime)
        } else {
          stringResource(R.string.ago, formatTime(-remainingTime))
        },
      fontWeight = FontWeight.Bold,
      color =
        if (remainingTime < WARNING_THRESHOLD) LocalCustomColors.current.warningText
        else MaterialTheme.colorScheme.onBackground)
  }
}

private fun getRemainingTime(advertisedStartSeconds: Long): Long {
  val currentTimeSeconds = System.currentTimeMillis() / SECONDS_IN_MILLIS
  return advertisedStartSeconds - currentTimeSeconds
}

private fun formatTime(seconds: Long): String {
  val duration = Duration.ofSeconds(seconds)
  val time = LocalTime.ofSecondOfDay(seconds)

  return when {
    duration.toHours() > 0 -> time.format(DateTimeFormatter.ofPattern("H'h' m'm'"))
    duration.toMinutes() > 0 -> {
      if (duration.toMinutes() > COUNT_THRESHOLD_MINUTES) {
        time.format(DateTimeFormatter.ofPattern("m'm'"))
      } else {
        time.format(DateTimeFormatter.ofPattern("m'm' s's'"))
      }
    }
    else -> time.format(DateTimeFormatter.ofPattern("s's'"))
  }
}
