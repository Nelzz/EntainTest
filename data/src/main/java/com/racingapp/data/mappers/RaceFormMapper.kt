/* (C) 2025. All rights reserved. */
package com.racingapp.data.mappers

import com.racingapp.data.models.RaceForm
import com.racingapp.domain.models.RaceInformation
import javax.inject.Inject

class RaceFormMapper
@Inject
constructor(
  private val distanceTypeMapper: DistanceTypeMapper,
  private val trackConditionMapper: TrackConditionMapper,
  private val weatherMapper: WeatherMapper
) {
  fun toDomain(raceFormDto: RaceForm?): RaceInformation {
    return RaceInformation(
      distance = raceFormDto?.distance,
      distanceDetails = distanceTypeMapper.toDomain(raceFormDto?.distanceType),
      distanceTypeId = raceFormDto?.distanceTypeId,
      trackDetails = trackConditionMapper.toDomain(raceFormDto?.trackCondition),
      trackConditionId = raceFormDto?.trackConditionId,
      weatherDetails = raceFormDto?.weather?.let { weatherMapper.toDomain(it) },
      weatherId = raceFormDto?.weatherId,
      raceComment = raceFormDto?.raceComment,
      additionalData = raceFormDto?.additionalData,
      generated = raceFormDto?.generated,
      silkBaseUrl = raceFormDto?.silkBaseUrl,
      raceCommentAlternative = raceFormDto?.raceCommentAlternative)
  }
}
