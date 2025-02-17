/* (C) 2025. All rights reserved. */
package com.racingapp.feature_dashboard.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.racingapp.feature_dashboard.DashboardScreen
import com.racingapp.feature_dashboard.navigation.destinations.DashBoardDestination

fun NavGraphBuilder.dashboardNavGraph() {
  composable<DashBoardDestination>() { DashboardScreen() }
}
