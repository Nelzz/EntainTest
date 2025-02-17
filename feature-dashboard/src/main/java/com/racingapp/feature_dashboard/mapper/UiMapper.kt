/* (C) 2025. All rights reserved. */
package com.racingapp.feature_dashboard.mapper

import com.racingapp.domain.models.Category
import com.racingapp.domain.models.RaceDetails
import com.racingapp.feature_dashboard.model.DashboardModel
import javax.inject.Inject

class UiMapper @Inject constructor(private val categoryUiMapper: CategoryUiMapper) {
  fun toDashboardModel(raceDetails: RaceDetails): DashboardModel {
    val category = Category.entries.first { it.id == raceDetails.categoryId }
    val categoryUI = categoryUiMapper.fromDomain(category)
    return DashboardModel(
      id = raceDetails.raceId,
      advertiseStartInSeconds = raceDetails.advertisedStart.seconds,
      meetingName = raceDetails.meetingName,
      raceNumber = raceDetails.raceNumber,
      categoryId = raceDetails.categoryId,
      categoryUI = categoryUI)
  }
}
