/* (C) 2025. All rights reserved. */
package com.racingapp.feature_dashboard.viewmodel

import androidx.annotation.RestrictTo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.racingapp.core.PollingManager
import com.racingapp.domain.logger.Logger
import com.racingapp.domain.models.Category
import com.racingapp.domain.usecases.PollingUseCase
import com.racingapp.domain.usecases.RetrieveUseCase
import com.racingapp.feature_dashboard.mapper.CategoryUiMapper
import com.racingapp.feature_dashboard.mapper.UiMapper
import com.racingapp.feature_dashboard.model.CategoryUI
import com.racingapp.feature_dashboard.model.DashboardModel
import com.racingapp.network.NetworkMonitor
import com.racingapp.ui_common.UiState
import com.racingapp.utils.DispatcherProvider
import com.racingapp.utils.NetworkException
import com.racingapp.utils.NetworkExceptionType
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class DashboardViewModel
@Inject
constructor(
  private val retrieveUseCase: RetrieveUseCase,
  private val pollingUseCase: PollingUseCase,
  private val uiMapper: UiMapper,
  private val networkMonitor: NetworkMonitor,
  private val categoryUiMapper: CategoryUiMapper,
  private val dispatcherProvider: DispatcherProvider,
  private val logger: Logger
) : ViewModel() {

  private val _state = MutableStateFlow<UiState<List<DashboardModel>>>(UiState.Loading)
  val state = _state.asStateFlow()

  private val cachedData = mutableListOf<DashboardModel>()

  private val _categories =
    MutableStateFlow(Category.entries.map { categoryUiMapper.fromDomain(it) })
  val categories: StateFlow<List<CategoryUI>> = _categories

  private val _selectedCategories = MutableStateFlow(_categories.value.toSet())
  val selectedCategories: StateFlow<Set<CategoryUI>> = _selectedCategories

  init {
    viewModelScope.launch(dispatcherProvider.io) {
      retrieveUseCase.getRaces().collectLatest {
        it.fold(
          onSuccess = {
            if (it.isEmpty()) return@fold

            val uiModels = it.map { item -> uiMapper.toDashboardModel(item) }
            cachedData.apply {
              clear()
              addAll(uiModels)
            }
            filterRaces()
          },
          onFailure = { _state.emit(UiState.Error(Exception(it.message))) })
      }
    }

    viewModelScope.launch(dispatcherProvider.io) {
      networkMonitor.observeNetwork().collectLatest {
        if (it) {
          fetch()
          startPolling()
        } else {

          logger.info(Exception("No internet connection").toString())
        }
      }
    }
  }

  fun toggleCategory(category: CategoryUI) {
    _selectedCategories.value =
      _selectedCategories.value.toMutableSet().apply {
        if (contains(category)) remove(category) else add(category)
      }
    if (!networkMonitor.isConnected) return
    filterRaces()
  }

  private fun filterRaces() {
    val currentRaces = cachedData
    val filteredRaces =
      if (_selectedCategories.value.isEmpty()) {
          currentRaces
        } else {
          currentRaces.filter { race -> _selectedCategories.value.any { it.id == race.categoryId } }
        }
        .take(MAX_LIMIT_ITEMS)
    _state.value = UiState.Success(filteredRaces)
  }

  private fun startPolling() {
    if (!networkMonitor.isConnected) {
      _state.value = UiState.Error(NetworkException(NetworkExceptionType.NoInternet))
      return
    }
    pollingUseCase.startPolling(PollingManager.INTERVAL_IN_MILLIS)
  }

  private fun stopPolling() {
    pollingUseCase.stopPolling()
  }

  fun refresh() {
    _state.value = UiState.Loading
    if (!networkMonitor.isConnected) {
      viewModelScope.launch {
        delay(DELAY)
        _state.value = UiState.Error(NetworkException(NetworkExceptionType.NoInternet))
      }
      return
    }

    fetch()
  }

  private fun fetch() {
    viewModelScope.launch(dispatcherProvider.io) { retrieveUseCase.manualFetch() }
  }

  @RestrictTo(RestrictTo.Scope.TESTS)
  public override fun onCleared() {
    super.onCleared()
    stopPolling()
  }

  companion object {
    private const val MAX_LIMIT_ITEMS = 5
    private const val DELAY = 300L
  }
}
