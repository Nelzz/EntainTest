/* (C) 2025. All rights reserved. */
package com.racingapp.challenge

import app.cash.turbine.test
import com.racingapp.challenge.ui.theme.ThemeMode
import com.racingapp.challenge.ui.theme.ThemePreferences
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ThemeViewModelTest {

  @OptIn(ExperimentalCoroutinesApi::class) @get:Rule var mainCoroutineRule = IOCoroutineRule()

  private lateinit var viewModel: ThemeViewModel
  private lateinit var themePreferences: ThemePreferences
  @OptIn(ExperimentalCoroutinesApi::class) private val testDispatcher = mainCoroutineRule.dispatcher

  @Before
  fun setup() {
    themePreferences = mockk(relaxed = true)
    every { themePreferences.themeMode } returns MutableStateFlow(ThemeMode.AUTO)
    viewModel = ThemeViewModel(themePreferences, testDispatcher)
  }

  @Test
  fun `Given ThemeMode is set when setThemeMode is called then themeMode is updated`() = runTest {
    val expectedTheme = ThemeMode.DARK

    viewModel.setThemeMode(expectedTheme)

    viewModel.themeMode.test {
      assertEquals(awaitItem(), ThemeMode.AUTO)
      assertEquals(awaitItem(), expectedTheme)
    }
    coVerify { themePreferences.saveTheme(expectedTheme) }
  }

  @Test
  fun `Given themePreferences emits Light mode when ViewModel is initialized then state is updated`() =
    runTest {
      val expectedTheme = ThemeMode.LIGHT
      every { themePreferences.themeMode } returns MutableStateFlow(expectedTheme)

      viewModel = ThemeViewModel(themePreferences, testDispatcher)
      viewModel.themeMode.test {
        assertEquals(awaitItem(), ThemeMode.AUTO)
        assertEquals(awaitItem(), expectedTheme)
      }
    }
}
