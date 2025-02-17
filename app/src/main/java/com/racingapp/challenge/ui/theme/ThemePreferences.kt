/* (C) 2025. All rights reserved. */
package com.racingapp.challenge.ui.theme

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class ThemePreferences @Inject constructor(@ApplicationContext private val context: Context) {
  private val Context.dataStore by preferencesDataStore(name = "theme_prefs")

  private val themeKey = intPreferencesKey("theme_mode")

  val themeMode: Flow<ThemeMode> =
    context.dataStore.data.map { preferences ->
      val modeValue = preferences[themeKey] ?: ThemeMode.AUTO.ordinal
      ThemeMode.entries[modeValue]
    }

  suspend fun saveTheme(mode: ThemeMode) {
    context.dataStore.edit { preferences -> preferences[themeKey] = mode.ordinal }
  }
}

enum class ThemeMode {
  LIGHT,
  DARK,
  AUTO
}
