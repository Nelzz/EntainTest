/* (C) 2025. All rights reserved. */
package com.racingapp.feature_dashboard

import androidx.compose.ui.test.junit4.createComposeRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.MockKAnnotations
import org.junit.Before
import org.junit.Rule

@HiltAndroidTest
class DashboardViewTest {

  @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  @get:Rule(order = 1) val composeTestRule = createComposeRule()

  @Before
  fun setup() {
    hiltRule.inject()
    MockKAnnotations.init(this, relaxed = true)
  }
}
