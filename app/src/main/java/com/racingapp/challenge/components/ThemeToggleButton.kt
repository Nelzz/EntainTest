/* (C) 2025. All rights reserved. */
package com.racingapp.challenge.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.racingapp.challenge.R
import com.racingapp.challenge.ui.theme.ThemeMode

@Composable
fun ThemeToggleButton(themeMode: ThemeMode, onSelectTheme: (ThemeMode) -> Unit) {
  val icon =
    when (themeMode) {
      ThemeMode.LIGHT -> painterResource(R.drawable.baseline_wb_sunny_24)
      ThemeMode.DARK -> painterResource(R.drawable.baseline_nightlight_24)
      ThemeMode.AUTO -> painterResource(R.drawable.baseline_brightness_medium_24)
    }

  IconButton(
    onClick = {
      val newMode =
        when (themeMode) {
          ThemeMode.LIGHT -> ThemeMode.DARK
          ThemeMode.DARK -> ThemeMode.AUTO
          ThemeMode.AUTO -> ThemeMode.LIGHT
        }
      onSelectTheme(newMode)
    }) {
      Icon(icon, contentDescription = stringResource(R.string.toggle_theme))
    }
}
