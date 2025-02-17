/* (C) 2025. All rights reserved. */
package com.racingapp.challenge.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.racingapp.challenge.R
import com.racingapp.challenge.components.ThemeToggleButton
import com.racingapp.challenge.ui.theme.ThemeMode
import com.racingapp.feature_dashboard.navigation.dashboardNavGraph
import com.racingapp.feature_dashboard.navigation.destinations.DashBoardDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
  themeMode: ThemeMode,
  navController: NavHostController,
  onSelectTheme: (ThemeMode) -> Unit
) {
  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      TopAppBar(
        colors =
          TopAppBarDefaults.topAppBarColors(
            titleContentColor = MaterialTheme.colorScheme.primary,
          ),
        title = { Text(stringResource(id = R.string.app_name)) },
        actions = { ThemeToggleButton(themeMode, onSelectTheme) })
    }) { padding ->
      NavHost(
        navController = navController,
        startDestination = DashBoardDestination,
        modifier = Modifier.padding(padding)) {
          dashboardNavGraph()
        }
    }
}
