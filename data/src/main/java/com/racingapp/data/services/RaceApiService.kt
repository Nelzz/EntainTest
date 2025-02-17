/* (C) 2025. All rights reserved. */
package com.racingapp.data.services

import com.racingapp.data.models.NextRacesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RaceApiService {
  @GET("v1/racing/")
  suspend fun fetchRaces(
    @Query("method") method: String = "nextraces",
    @Query("count") count: Int = 30
  ): NextRacesResponse
}
