/* (C) 2025. All rights reserved. */
package com.racingapp.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DistanceType(
  @Json(name = "id") val id: String,
  @Json(name = "name") val name: String,
  @Json(name = "short_name") val shortName: String
)
