/* (C) 2025. All rights reserved. */
package com.racingapp.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AdvertisedStart(@Json(name = "seconds") val seconds: Long)
