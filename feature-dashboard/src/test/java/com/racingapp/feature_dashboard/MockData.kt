/* (C) 2025. All rights reserved. */
package com.racingapp.feature_dashboard

import com.racingapp.domain.models.AdvertisedStartDetails
import com.racingapp.domain.models.Category
import com.racingapp.domain.models.RaceDetails
import com.racingapp.feature_dashboard.model.CategoryUI
import io.mockk.mockk

val testRaceDetails =
  listOf(
    RaceDetails(
      "1",
      "Horse Race 1",
      1,
      "M1",
      "Meeting 1",
      Category.HORSE.id,
      AdvertisedStartDetails(10L),
      mockk(),
      "V1",
      "Venue 1",
      "State 1",
      "Country 1"),
    RaceDetails(
      "2",
      "Horse Race 2",
      2,
      "M2",
      "Meeting 2",
      Category.HARNESS.id,
      AdvertisedStartDetails(20L),
      mockk(),
      "V2",
      "Venue 2",
      "State 2",
      "Country 2"),
    RaceDetails(
      "3",
      "Horse Race 3",
      3,
      "M3",
      "Meeting 3",
      Category.HORSE.id,
      AdvertisedStartDetails(30L),
      mockk(),
      "V3",
      "Venue 3",
      "State 3",
      "Country 3"),
    RaceDetails(
      "4",
      "Harness Race 1",
      4,
      "M4",
      "Meeting 4",
      Category.GREYHOUND.id,
      AdvertisedStartDetails(40L),
      mockk(),
      "V4",
      "Venue 4",
      "State 4",
      "Country 4"),
    RaceDetails(
      "5",
      "Harness Race 2",
      5,
      "M5",
      "Meeting 5",
      Category.HORSE.id,
      AdvertisedStartDetails(50L),
      mockk(),
      "V5",
      "Venue 5",
      "State 5",
      "Country 5"),
    RaceDetails(
      "6",
      "Harness Race 3",
      6,
      "M6",
      "Meeting 6",
      Category.HARNESS.id,
      AdvertisedStartDetails(60L),
      mockk(),
      "V6",
      "Venue 6",
      "State 6",
      "Country 6"),
    RaceDetails(
      "7",
      "Harness Race 4",
      7,
      "M7",
      "Meeting 7",
      Category.HARNESS.id,
      AdvertisedStartDetails(70L),
      mockk(),
      "V7",
      "Venue 7",
      "State 7",
      "Country 7"),
    RaceDetails(
      "8",
      "Greyhound Race 1",
      8,
      "M8",
      "Meeting 8",
      Category.GREYHOUND.id,
      AdvertisedStartDetails(80L),
      mockk(),
      "V8",
      "Venue 8",
      "State 8",
      "Country 8"),
    RaceDetails(
      "9",
      "Greyhound Race 2",
      9,
      "M9",
      "Meeting 9",
      Category.HARNESS.id,
      AdvertisedStartDetails(90L),
      mockk(),
      "V9",
      "Venue 9",
      "State 9",
      "Country 9"),
    RaceDetails(
      "10",
      "Greyhound Race 3",
      10,
      "M10",
      "Meeting 10",
      Category.GREYHOUND.id,
      AdvertisedStartDetails(100L),
      mockk(),
      "V10",
      "Venue 10",
      "State 10",
      "Country 10"))

fun mockRaceDetailsGenerator(count: Int = 10): List<RaceDetails> {
  val testRaceDetails =
    List(count) { index ->
      RaceDetails(
        raceId = index.toString(),
        raceName = "Race $index",
        raceNumber = index + 1,
        meetingId = "M$index",
        meetingName = "Meeting $index",
        categoryId = Category.entries[index % 3].id, // Use the actual category ID
        advertisedStart = AdvertisedStartDetails(index * 10L),
        raceInformation = mockk(),
        venueId = "V$index",
        venueName = "Venue $index",
        venueState = "State $index",
        venueCountry = "Country $index")
    }
  return testRaceDetails
}

val testCategories =
  listOf(
    CategoryUI(name = 1, id = Category.HORSE.id),
    CategoryUI(name = 2, id = Category.HARNESS.id),
    CategoryUI(name = 3, id = Category.GREYHOUND.id))

val fakeStringRes =
  mapOf(Category.HORSE.id to 1, Category.HARNESS.id to 2, Category.GREYHOUND.id to 3)
