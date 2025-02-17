/* (C) 2025. All rights reserved. */
package com.racingapp.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RaceForm(
  @Json(name = "distance") val distance: Int?,
  @Json(name = "distance_type") val distanceType: DistanceType?,
  @Json(name = "distance_type_id") val distanceTypeId: String,
  @Json(name = "track_condition") val trackCondition: TrackCondition?,
  @Json(name = "track_condition_id") val trackConditionId: String?,
  @Json(name = "weather") val weather: Weather?,
  @Json(name = "weather_id") val weatherId: String?,
  @Json(name = "race_comment") val raceComment: String?,
  @Json(name = "additional_data") val additionalData: String?,
  @Json(name = "generated") val generated: Int?,
  @Json(name = "silk_base_url") val silkBaseUrl: String?,
  @Json(name = "race_comment_alternative") val raceCommentAlternative: String?
)
