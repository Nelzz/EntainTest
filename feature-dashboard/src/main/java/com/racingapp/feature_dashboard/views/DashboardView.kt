/* (C) 2025. All rights reserved. */
package com.racingapp.feature_dashboard.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.racingapp.feature_dashboard.components.ErrorContent
import com.racingapp.feature_dashboard.components.FilterSection
import com.racingapp.feature_dashboard.components.LoadingContent
import com.racingapp.feature_dashboard.components.RaceItem
import com.racingapp.feature_dashboard.components.SnackBarContent
import com.racingapp.feature_dashboard.viewmodel.DashboardViewModel
import com.racingapp.ui_common.UiState
import com.racingapp.ui_common.theme.LocalCustomColors

@Composable
fun DashboardView(viewModel: DashboardViewModel = hiltViewModel()) {
  val snackbarHostState = remember { SnackbarHostState() }
  val categories = viewModel.categories.collectAsStateWithLifecycle().value
  val selectedCategories = viewModel.selectedCategories.collectAsStateWithLifecycle().value
  Column(modifier = Modifier.fillMaxSize().testTag("test-dashboard-view")) {
    FilterSection(
      categories = categories,
      selectedCategories,
      onFilterSelected = { category -> viewModel.toggleCategory(category) })

    Box(modifier = Modifier.fillMaxSize()) {
      when (val state = viewModel.state.collectAsState().value) {
        is UiState.Loading -> {
          LoadingContent()
        }

        is UiState.Error -> {
          ErrorContent(state) {
            snackbarHostState.showSnackbar(it, duration = SnackbarDuration.Indefinite)
          }
        }
        is UiState.Success -> {
          LazyColumn(
            modifier =
              Modifier.padding(6.dp).background(LocalCustomColors.current.cardBackground)) {
              items(items = state.data, key = { it.id }) { race -> RaceItem(race) }
            }
        }
      }

      SnackbarHost(
        hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter)) {
          SnackBarContent(it) { viewModel.refresh() }
        }
    }
  }
}
