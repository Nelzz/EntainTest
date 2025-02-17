/* (C) 2025. All rights reserved. */
package com.racingapp.challenge

import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.racingapp.challenge.di.AppModule
import com.racingapp.challenge.screens.MainScreen
import com.racingapp.challenge.ui.theme.ThemeMode
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@UninstallModules(AppModule::class)
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MainScreenTest {

  @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  @get:Rule(order = 1) val composeTestRule = createAndroidComposeRule<MainActivity>()
  private lateinit var navController: TestNavHostController

  @Before
  fun setupAppNavHost() {
    hiltRule.inject()
    composeTestRule.activity.setContent {
      navController = TestNavHostController(LocalContext.current)
      navController.navigatorProvider.addNavigator(ComposeNavigator())
      MainScreen(themeMode = ThemeMode.AUTO, onSelectTheme = {}, navController = navController)
    }
  }

  @Test
  fun appNavHost_verifyStartDestination() {
    composeTestRule.onNodeWithTag("test-dashboard-view").assertIsDisplayed()
  }
}
