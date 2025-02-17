/* (C) 2025. All rights reserved. */
package com.racingapp.feature_dashboard

import com.racingapp.domain.models.AdvertisedStartDetails
import com.racingapp.domain.models.Category
import com.racingapp.domain.models.DistanceDetails
import com.racingapp.domain.models.RaceDetails
import com.racingapp.domain.models.RaceInformation
import com.racingapp.domain.models.TrackDetails
import com.racingapp.domain.models.WeatherDetails
import com.racingapp.feature_dashboard.mapper.CategoryUiMapper
import com.racingapp.feature_dashboard.mapper.UiMapper
import com.racingapp.feature_dashboard.model.CategoryUI
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class UiMapperTest {

  private var categoryUiMapper: CategoryUiMapper = mockk(relaxed = true)

  private lateinit var uiMapper: UiMapper

  @Before
  fun setup() {
    uiMapper = UiMapper(categoryUiMapper)
  }

  @Test
  fun `toDashboardModel correctly maps RaceDetails to DashboardModel`() {

    val category = Category.entries.first()
    val categoryUI = CategoryUI(name = 0, id = category.id, iconRes = 1)

    val raceDetails =
      RaceDetails(
        raceId = "123",
        advertisedStart = AdvertisedStartDetails(1739589900L),
        meetingName = "Test Meeting",
        raceNumber = 5,
        categoryId = category.id,
        raceName = "Race 1",
        meetingId = "Meeting123",
        raceInformation =
          RaceInformation(
            distance = 1600,
            distanceDetails = DistanceDetails("m", "Meters", shortName = ""),
            distanceTypeId = "type123",
            trackDetails = TrackDetails("Turf", "Green", shortName = ""),
            trackConditionId = "condition123",
            weatherDetails = WeatherDetails("Sunny", "Clear", "sunny_icon", iconUri = ""),
            weatherId = "weather123",
            raceComment = "Exciting race",
            additionalData = "{}",
            generated = 1,
            silkBaseUrl = "https://silks.com",
            raceCommentAlternative = "Another race comment"),
        venueId = "Venue123",
        venueName = "Melbourne Racecourse",
        venueState = "VIC",
        venueCountry = "Australia")

    every { categoryUiMapper.fromDomain(category) } returns categoryUI

    val dashboardModel = uiMapper.toDashboardModel(raceDetails)

    assertEquals("123", dashboardModel.id)
    assertEquals(1739589900L, dashboardModel.advertiseStartInSeconds)
    assertEquals("Test Meeting", dashboardModel.meetingName)
    assertEquals(5, dashboardModel.raceNumber)
    assertEquals(category.id, dashboardModel.categoryId)
    assertEquals(categoryUI, dashboardModel.categoryUI)

    verify { categoryUiMapper.fromDomain(category) }
  }
}
