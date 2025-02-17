/* (C) 2025. All rights reserved. */
import app.cash.turbine.test
import app.cash.turbine.turbineScope
import com.racingapp.core.PollingManager
import com.racingapp.data.mappers.RaceSummaryMapper
import com.racingapp.data.models.AdvertisedStart
import com.racingapp.data.models.DistanceType
import com.racingapp.data.models.NextRacesData
import com.racingapp.data.models.NextRacesResponse
import com.racingapp.data.models.RaceForm
import com.racingapp.data.models.RaceSummary
import com.racingapp.data.models.TrackCondition
import com.racingapp.data.models.Weather
import com.racingapp.data.repositories.RemoteRepository
import com.racingapp.data.services.RaceApiService
import com.racingapp.domain.logger.Logger
import com.racingapp.domain.models.AdvertisedStartDetails
import com.racingapp.domain.models.DistanceDetails
import com.racingapp.domain.models.RaceDetails
import com.racingapp.domain.models.RaceInformation
import com.racingapp.domain.models.TrackDetails
import com.racingapp.domain.models.WeatherDetails
import com.racingapp.utils.NetworkException
import com.racingapp.utils.NetworkExceptionType
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody
import okio.IOException
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class RemoteRepositoryTest {

  private lateinit var repository: RemoteRepository

  private val apiService: RaceApiService = mockk()
  private val mapper: RaceSummaryMapper = mockk()
  private val pollingManager: PollingManager = mockk(relaxed = true)
  private val logger: Logger = mockk(relaxed = true)

  @OptIn(ExperimentalCoroutinesApi::class)
  @Before
  fun setup() {
    repository = RemoteRepository(apiService, mapper, pollingManager, logger)
    Dispatchers.setMain(StandardTestDispatcher())
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun `fetch races successfully updates state`() = runTest {
    val fakeRaceSummary = mockData()
    val fakeRace = mockRaceDetails()

    every { mapper.toDomain(any()) } returns fakeRace

    val response =
      NextRacesResponse(
        status = 200,
        data =
          NextRacesData(
            nextToGoIds = listOf("race1"), raceSummaries = mapOf("race1" to fakeRaceSummary)),
        message = "Success")

    coEvery { apiService.fetchRaces() } returns response

    repository.races.test {
      repository.getUpcomingRaces()
      val result = awaitItem()
      assert(result.isSuccess)
      assert(result.getOrNull() == listOf(fakeRace))
    }
  }

  private fun mockData(): RaceSummary {
    val fakeRaceSummary =
      RaceSummary(
        raceId = "race-123",
        raceName = "Test Race",
        raceNumber = 1,
        meetingId = "meeting-001",
        meetingName = "Sample Meeting",
        categoryId = "4a2788f8-e825-4d36-9894-efd4baf1cfae",
        advertisedStart = AdvertisedStart(seconds = 1739589900),
        raceForm =
          RaceForm(
            distance = 1200,
            distanceType = DistanceType("id", "Metres", "m"),
            distanceTypeId = "id",
            trackCondition = TrackCondition("id", "Fast", "fast"),
            trackConditionId = "id",
            weather = Weather("id", "Fine", "fine", "icon"),
            weatherId = "id",
            raceComment = "Some comment",
            additionalData = "{}",
            generated = 1,
            silkBaseUrl = "url",
            raceCommentAlternative = "Alternative comment"),
        venueId = "venue-001",
        venueName = "Test Venue",
        venueState = "NY",
        venueCountry = "USA")

    return fakeRaceSummary
  }

  private fun mockRaceDetails(): RaceDetails {
    val mappedRace =
      RaceDetails(
        raceId = "race-123",
        raceName = "Test Race",
        raceNumber = 1,
        meetingId = "meeting-001",
        meetingName = "Sample Meeting",
        categoryId = "4a2788f8-e825-4d36-9894-efd4baf1cfae",
        advertisedStart = AdvertisedStartDetails(seconds = 1739589900),
        raceInformation =
          RaceInformation(
            distance = 1200,
            distanceDetails = DistanceDetails("id", "Metres", "m"),
            distanceTypeId = "id",
            trackDetails = TrackDetails("id", "Fast", "fast"),
            trackConditionId = "id",
            weatherDetails = WeatherDetails("id", "Fine", "fine", "icon"),
            weatherId = "id",
            raceComment = "Some comment",
            additionalData = "{}",
            generated = 1,
            silkBaseUrl = "url",
            raceCommentAlternative = "Alternative comment"),
        venueId = "venue-001",
        venueName = "Test Venue",
        venueState = "NY",
        venueCountry = "USA")
    return mappedRace
  }

  @Test
  fun `fetch races fails with no internet`() =
    runTest(UnconfinedTestDispatcher()) {
      coEvery { apiService.fetchRaces() } throws IOException()

      repository.races.test {
        repository.getUpcomingRaces()
        val result = awaitItem()
        assert(result.isFailure)
        assert(result.exceptionOrNull() is NetworkException)
        assert(
          (result.exceptionOrNull() as NetworkException).type is NetworkExceptionType.NoInternet)
      }
    }

  @Test
  fun `fetch races fails with HTTP error`() = runTest {
    turbineScope {
      coEvery { apiService.fetchRaces() } throws
        HttpException(Response.error<ResponseBody>(500, ResponseBody.create(null, "Server error")))
      repository.races.test {
        repository.getUpcomingRaces()
        val result = awaitItem()
        assert(result.isFailure)
        assert(result.exceptionOrNull() is NetworkException)
        assert(
          (result.exceptionOrNull() as NetworkException).type is NetworkExceptionType.ServerError)
      }
      verify { logger.error(any()) }
    }
  }

  @Test
  fun `fetch races fails with timeout`() = runTest {
    coEvery { apiService.fetchRaces() } throws SocketTimeoutException()

    repository.races.test {
      repository.getUpcomingRaces()
      val result = awaitItem()
      assert(result.isFailure)
      assert(result.exceptionOrNull() is NetworkException)
      assert((result.exceptionOrNull() as NetworkException).type is NetworkExceptionType.Timeout)
    }

    verify { logger.error(any()) }
  }

  @Test
  fun `fetch races fails with UnknownHostException`() = runTest {
    coEvery { apiService.fetchRaces() } throws UnknownHostException()

    repository.races.test {
      repository.getUpcomingRaces()
      val result = awaitItem()
      assert(result.isFailure)
      assert(result.exceptionOrNull() is NetworkException)
      assert(
        (result.exceptionOrNull() as NetworkException).type is NetworkExceptionType.UnknownHost)
    }

    verify { logger.error(any()) }
  }

  @Test
  fun `fetch races fails with SocketException`() = runTest {
    coEvery { apiService.fetchRaces() } throws SocketException()

    repository.races.test {
      repository.getUpcomingRaces()
      val result = awaitItem()
      assert(result.isFailure)
      assert(result.exceptionOrNull() is NetworkException)
      assert(
        (result.exceptionOrNull() as NetworkException).type is NetworkExceptionType.ConnectionError)
    }

    verify { logger.error(any()) }
  }
}
