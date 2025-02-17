/* (C) 2025. All rights reserved. */
package com.racingapp.data.mappers

import com.racingapp.data.models.DistanceType
import com.racingapp.domain.models.DistanceDetails
import javax.inject.Inject

class DistanceTypeMapper @Inject constructor() {
  fun toDomain(distanceTypeDto: DistanceType?): DistanceDetails {
    return DistanceDetails(
      id = distanceTypeDto?.id,
      name = distanceTypeDto?.name,
      shortName = distanceTypeDto?.shortName)
  }
}
