/* (C) 2025. All rights reserved. */
import app.cash.turbine.test
import com.racingapp.domain.models.RaceDetails
import com.racingapp.domain.repositories.Repository
import com.racingapp.domain.usecases.RetrieveUseCase
import io.mockk.every
import io.mockk.mockk
import java.time.Instant
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.fail
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RetrieveUseCaseTest {

  private lateinit var retrieveUseCase: RetrieveUseCase
  private val repository: Repository = mockk()

  @Before
  fun setUp() {
    retrieveUseCase = RetrieveUseCase(repository)
  }

  @Test
  fun `getRaces filters out races that are more than one minute past the advertised start`() =
    runTest {
      val now = Instant.now()

      val validRace =
        mockk<RaceDetails> {
          every { advertisedStart.seconds } returns now.toEpochMilli() // Valid race
          every { meetingName } returns "Valid Race Meeting"
          every { raceNumber } returns 5
        }

      val expiredRace =
        mockk<RaceDetails> {
          every { advertisedStart.seconds } returns
            now.minusSeconds(120).toEpochMilli() // Expired race (2 minutes ago)
          every { meetingName } returns "Expired Race Meeting"
          every { raceNumber } returns 10
        }

      val raceList = listOf(validRace, expiredRace)
      every { repository.races } returns flowOf(Result.success(raceList))

      val resultFlow = retrieveUseCase.getRaces()

      resultFlow.test {
        val result = awaitItem()
        assertEquals(true, result.isSuccess)
        val filteredRaces = result.getOrThrow()

        assertEquals(1, filteredRaces.size)
        assertEquals("Valid Race Meeting", filteredRaces[0].meetingName)
        assertEquals(5, filteredRaces[0].raceNumber)

        cancelAndConsumeRemainingEvents()
      }
    }

  @Test
  fun `getRaces excludes races more than 1 minute past advertised start`() = runTest {
    val now = Instant.now()

    val futureRace =
      mockk<RaceDetails> {
        every { advertisedStart.seconds } returns
          now.plusSeconds(300).toEpochMilli() // Starts in 5 min
        every { meetingName } returns "Future Race"
        every { raceNumber } returns 1
      }

    val barelyValidRace =
      mockk<RaceDetails> {
        every { advertisedStart.seconds } returns
          now.minusSeconds(59).toEpochMilli() // 59s ago (Valid)
        every { meetingName } returns "Barely Valid Race"
        every { raceNumber } returns 2
      }

    val expiredRace =
      mockk<RaceDetails> {
        every { advertisedStart.seconds } returns
          now.minusSeconds(61).toEpochMilli() // 61s ago (Should be removed)
        every { meetingName } returns "Expired Race"
        every { raceNumber } returns 3
      }

    every { repository.races } returns
      flowOf(Result.success(listOf(futureRace, barelyValidRace, expiredRace)))

    val result = retrieveUseCase.getRaces().first()

    result.fold(
      onSuccess = { filteredRaces ->
        assertEquals(2, filteredRaces.size)
        assertEquals(true, filteredRaces.any { it.meetingName == "Future Race" })
        assertEquals(true, filteredRaces.any { it.meetingName == "Barely Valid Race" })
        assertEquals(false, filteredRaces.any { it.meetingName == "Expired Race" })
      },
      onFailure = { fail("Expected success but got failure: $it") })
  }

  @Test
  fun `getRaces returns races sorted by advertised start time in ascending order`() = runTest {
    val now = Instant.now()

    val race1 =
      mockk<RaceDetails> {
        every { advertisedStart.seconds } returns
          now.plusSeconds(600).toEpochMilli() // 10 mins from now
        every { meetingName } returns "Race 1"
        every { raceNumber } returns 1
      }

    val race2 =
      mockk<RaceDetails> {
        every { advertisedStart.seconds } returns
          now.plusSeconds(300).toEpochMilli() // 5 mins from now
        every { meetingName } returns "Race 2"
        every { raceNumber } returns 2
      }

    val race3 =
      mockk<RaceDetails> {
        every { advertisedStart.seconds } returns
          now.plusSeconds(900).toEpochMilli() // 15 mins from now
        every { meetingName } returns "Race 3"
        every { raceNumber } returns 3
      }

    val expiredRace =
      mockk<RaceDetails> {
        every { advertisedStart.seconds } returns now.minusSeconds(120).toEpochMilli() // Expired
        every { meetingName } returns "Expired Race"
        every { raceNumber } returns 4
      }

    every { repository.races } returns
      flowOf(Result.success(listOf(race1, race2, race3, expiredRace)))

    val result = retrieveUseCase.getRaces().first()

    result.fold(
      onSuccess = { filteredRaces ->
        assertEquals(3, filteredRaces.size)

        val sortedRaceNames = filteredRaces.map { it.meetingName }
        val expectedOrder = listOf("Race 2", "Race 1", "Race 3") // Sorted by advertised start time

        assertEquals(expectedOrder, sortedRaceNames)
      },
      onFailure = { fail("Expected success but got failure: $it") })
  }
}
