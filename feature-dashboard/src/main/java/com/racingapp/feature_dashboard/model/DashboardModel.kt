/* (C) 2025. All rights reserved. */
package com.racingapp.feature_dashboard.model

data class DashboardModel(
  val id: String,
  val advertiseStartInSeconds: Long,
  val meetingName: String,
  val raceNumber: Int,
  val categoryId: String,
  val categoryUI: CategoryUI
)
