/* (C) 2025. All rights reserved. */
package com.racingapp.domain.models

data class RaceInformation(
  val distance: Int?,
  val distanceDetails: DistanceDetails?,
  val distanceTypeId: String?,
  val trackDetails: TrackDetails?,
  val trackConditionId: String?,
  val weatherDetails: WeatherDetails?,
  val weatherId: String?,
  val raceComment: String?,
  val additionalData: String?,
  val generated: Int?,
  val silkBaseUrl: String?,
  val raceCommentAlternative: String?
)
