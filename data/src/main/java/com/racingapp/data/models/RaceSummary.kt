/* (C) 2025. All rights reserved. */
package com.racingapp.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RaceSummary(
  @Json(name = "race_id") val raceId: String,
  @Json(name = "race_name") val raceName: String,
  @Json(name = "race_number") val raceNumber: Int,
  @Json(name = "meeting_id") val meetingId: String,
  @Json(name = "meeting_name") val meetingName: String,
  @Json(name = "category_id") val categoryId: String,
  @Json(name = "advertised_start") val advertisedStart: AdvertisedStart,
  @Json(name = "race_form") val raceForm: RaceForm?,
  @Json(name = "venue_id") val venueId: String,
  @Json(name = "venue_name") val venueName: String,
  @Json(name = "venue_state") val venueState: String,
  @Json(name = "venue_country") val venueCountry: String
)
