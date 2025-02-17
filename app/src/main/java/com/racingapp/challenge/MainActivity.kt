/* (C) 2025. All rights reserved. */
package com.racingapp.challenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.racingapp.challenge.screens.MainScreen
import com.racingapp.challenge.ui.theme.NextRaceTheme
import com.racingapp.challenge.ui.theme.ThemeMode
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    installSplashScreen()
    setContent { NextRaceApp() }
  }
}

@Composable
fun NextRaceApp(viewModel: ThemeViewModel = hiltViewModel()) {
  val navController = rememberNavController()
  val themeMode by viewModel.themeMode.collectAsState(initial = ThemeMode.AUTO)
  val isSystemDark = isSystemInDarkTheme()
  val isDarkMode =
    remember(themeMode) {
      when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.AUTO -> isSystemDark
      }
    }

  NextRaceTheme(darkTheme = isDarkMode) {
    MainScreen(themeMode, navController, onSelectTheme = { viewModel.setThemeMode(it) })
  }
}
