/* (C) 2025. All rights reserved. */
package com.racingapp.data.mappers

import com.racingapp.data.models.RaceSummary
import com.racingapp.domain.models.RaceDetails
import javax.inject.Inject

class RaceSummaryMapper
@Inject
constructor(
  private val advertisedStartMapper: AdvertisedStartMapper,
  private val raceFormMapper: RaceFormMapper
) {
  fun toDomain(raceSummaryDto: RaceSummary): RaceDetails {
    return RaceDetails(
      raceId = raceSummaryDto.raceId,
      raceName = raceSummaryDto.raceName,
      raceNumber = raceSummaryDto.raceNumber,
      meetingId = raceSummaryDto.meetingId,
      meetingName = raceSummaryDto.meetingName,
      categoryId = raceSummaryDto.categoryId,
      advertisedStart = advertisedStartMapper.toDomain(raceSummaryDto.advertisedStart),
      raceInformation = raceFormMapper.toDomain(raceSummaryDto.raceForm),
      venueId = raceSummaryDto.venueId,
      venueName = raceSummaryDto.venueName,
      venueState = raceSummaryDto.venueState,
      venueCountry = raceSummaryDto.venueCountry)
  }
}
