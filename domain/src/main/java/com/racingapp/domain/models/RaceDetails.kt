/* (C) 2025. All rights reserved. */
package com.racingapp.domain.models

data class RaceDetails(
  val raceId: String,
  val raceName: String,
  val raceNumber: Int,
  val meetingId: String,
  val meetingName: String,
  val categoryId: String,
  val advertisedStart: AdvertisedStartDetails,
  val raceInformation: RaceInformation,
  val venueId: String,
  val venueName: String,
  val venueState: String,
  val venueCountry: String
)
