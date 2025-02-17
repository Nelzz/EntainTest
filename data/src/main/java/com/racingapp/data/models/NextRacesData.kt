/* (C) 2025. All rights reserved. */
package com.racingapp.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NextRacesData(
  @Json(name = "next_to_go_ids") val nextToGoIds: List<String>,
  @Json(name = "race_summaries") val raceSummaries: Map<String, RaceSummary>
)
