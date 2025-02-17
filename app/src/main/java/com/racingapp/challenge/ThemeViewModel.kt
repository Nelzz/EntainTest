/* (C) 2025. All rights reserved. */
package com.racingapp.challenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.racingapp.challenge.ui.theme.ThemeMode
import com.racingapp.challenge.ui.theme.ThemePreferences
import com.racingapp.utils.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ThemeViewModel
@Inject
constructor(
  private val themePreferences: ThemePreferences,
  private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

  private val _themeMode = MutableStateFlow(ThemeMode.AUTO)
  val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

  init {
    viewModelScope.launch(dispatcherProvider.io) {
      themePreferences.themeMode.collect { mode -> _themeMode.value = mode }
    }
  }

  fun setThemeMode(mode: ThemeMode) {
    viewModelScope.launch(dispatcherProvider.io) {
      _themeMode.value = mode
      themePreferences.saveTheme(mode)
    }
  }
}
