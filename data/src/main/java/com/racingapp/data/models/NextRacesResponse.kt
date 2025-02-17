/* (C) 2025. All rights reserved. */
package com.racingapp.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NextRacesResponse(
  @Json(name = "status") val status: Int,
  @Json(name = "data") val data: NextRacesData,
  @Json(name = "message") val message: String
)
