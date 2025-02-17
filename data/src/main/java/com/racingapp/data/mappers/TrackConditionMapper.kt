/* (C) 2025. All rights reserved. */
package com.racingapp.data.mappers

import com.racingapp.data.models.TrackCondition as TrackConditionDto
import com.racingapp.domain.models.TrackDetails
import javax.inject.Inject

class TrackConditionMapper @Inject constructor() {
  fun toDomain(trackConditionDto: TrackConditionDto?): TrackDetails {
    return TrackDetails(
      id = trackConditionDto?.id,
      name = trackConditionDto?.name,
      shortName = trackConditionDto?.shortName)
  }
}
