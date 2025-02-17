/* (C) 2025. All rights reserved. */
package com.racingapp.data.mappers

import com.racingapp.data.models.AdvertisedStart
import com.racingapp.domain.models.AdvertisedStartDetails
import javax.inject.Inject

class AdvertisedStartMapper @Inject constructor() {
  fun toDomain(advertisedStartDto: AdvertisedStart): AdvertisedStartDetails {
    return AdvertisedStartDetails(seconds = advertisedStartDto.seconds * 1000L)
  }
}
