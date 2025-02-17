/* (C) 2025. All rights reserved. */
package com.racingapp.data.mappers

import com.racingapp.data.models.Weather as WeatherDto
import com.racingapp.domain.models.WeatherDetails
import javax.inject.Inject

class WeatherMapper @Inject constructor() {
  fun toDomain(weatherDto: WeatherDto): WeatherDetails {
    return WeatherDetails(
      id = weatherDto.id,
      name = weatherDto.name,
      shortName = weatherDto.shortName,
      iconUri = weatherDto.iconUri)
  }
}
