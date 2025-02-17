/* (C) 2025. All rights reserved. */
package com.racingapp.feature_dashboard

import app.cash.turbine.test
import com.racingapp.domain.logger.Logger
import com.racingapp.domain.models.Category
import com.racingapp.domain.models.RaceDetails
import com.racingapp.domain.usecases.PollingUseCase
import com.racingapp.domain.usecases.RetrieveUseCase
import com.racingapp.feature_dashboard.mapper.CategoryUiMapper
import com.racingapp.feature_dashboard.mapper.UiMapper
import com.racingapp.feature_dashboard.model.CategoryUI
import com.racingapp.feature_dashboard.model.DashboardModel
import com.racingapp.feature_dashboard.viewmodel.DashboardViewModel
import com.racingapp.network.NetworkMonitor
import com.racingapp.ui_common.UiState
import com.racingapp.utils.NetworkException
import com.racingapp.utils.NetworkExceptionType
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DashboardViewModelTest {

  @ExperimentalCoroutinesApi @get:Rule var mainCoroutineRule = IOCoroutineRule()
  private val testDispatcher = mainCoroutineRule.dispatcher

  private lateinit var viewModel: DashboardViewModel

  private val retrieveUseCase: RetrieveUseCase = mockk(relaxed = true)
  private val pollingUseCase: PollingUseCase = mockk(relaxed = true)
  private val uiMapper: UiMapper = mockk(relaxed = true)
  private val networkMonitor: NetworkMonitor = mockk(relaxed = true)
  private val categoryUiMapper: CategoryUiMapper = mockk(relaxed = true)
  private val logger: Logger = mockk(relaxed = true)

  @Before
  fun setup() {
    every { networkMonitor.observeNetwork() } returns flowOf(true)
    every { networkMonitor.isConnected } returns true

    every { categoryUiMapper.fromDomain(any()) } answers
      {
        val category = firstArg<Category>()
        CategoryUI(name = fakeStringRes[category.id] ?: 0, id = category.id)
      }

    every { retrieveUseCase.getRaces() } returns flowOf(Result.success(emptyList()))

    every { networkMonitor.observeNetwork() } returns flow { emit(true) }

    every { networkMonitor.isConnected } returns true

    every { uiMapper.toDashboardModel(any()) } answers
      { raceDetails ->
        val race = raceDetails.invocation.args[0] as RaceDetails
        DashboardModel(
          id = race.raceId,
          advertiseStartInSeconds = race.advertisedStart.seconds,
          meetingName = race.meetingName,
          raceNumber = race.raceNumber,
          categoryId = race.categoryId,
          categoryUI =
            testCategories.find { it.id == race.categoryId }
              ?: CategoryUI(name = 999, id = race.categoryId))
      }

    viewModel =
      DashboardViewModel(
        retrieveUseCase,
        pollingUseCase,
        uiMapper,
        networkMonitor,
        categoryUiMapper,
        testDispatcher,
        logger)
  }

  @Test
  fun `initial state is loading`() = runTest {
    viewModel.state.test {
      assertTrue(awaitItem() is UiState.Loading)
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `successful data fetch updates state`() = runTest {
    val races = listOf(mockk<RaceDetails>(relaxed = true))
    every { retrieveUseCase.getRaces() } returns flowOf(Result.success(races))
    every { uiMapper.toDashboardModel(any()) } answers
      {
        DashboardModel(
          id = "1",
          meetingName = "Test",
          raceNumber = 1,
          categoryId = "9daef0d7-bf3c-4f50-921d-8e818c60fe61",
          advertiseStartInSeconds = 1000L,
          categoryUI = CategoryUI(1, "9daef0d7-bf3c-4f50-921d-8e818c60fe61"))
      }
    every { networkMonitor.observeNetwork() } returns flowOf(true)

    viewModel =
      DashboardViewModel(
        retrieveUseCase,
        pollingUseCase,
        uiMapper,
        networkMonitor,
        categoryUiMapper,
        testDispatcher,
        logger)

    viewModel.state.test {
      val awaitedItem = awaitItem()
      assert(awaitedItem is UiState.Loading)
      val successState = awaitItem()
      assert(successState is UiState.Success)
      assert((successState as UiState.Success).data.isNotEmpty())
      cancelAndIgnoreRemainingEvents()
    }
    verify { pollingUseCase.startPolling(eq(5000)) }
  }

  @Test
  fun `network disconnection stops polling`() = runTest {
    every { networkMonitor.observeNetwork() } returns flowOf(false)

    viewModel.onCleared()
    verify { pollingUseCase.stopPolling() }
  }

  @Test
  fun `toggleCategory updates selected categories`() = runTest {
    val category = CategoryUI(1, "Horse Racing")

    viewModel.toggleCategory(category)

    assertTrue(viewModel.selectedCategories.value.contains(category))
  }

  @Test
  fun `refresh fetches new data when online`() = runTest {
    every { networkMonitor.isConnected } returns true
    viewModel.refresh()
    advanceUntilIdle()
    coVerify { retrieveUseCase.manualFetch() }
  }

  @Test
  fun `refresh does nothing when offline`() = runTest {
    every { networkMonitor.isConnected } returns false
    viewModel.refresh()
    coVerify(exactly = 0) { retrieveUseCase.manualFetch() }
  }

  @Test
  fun `Given NoInternet exception when fetching data then emit Error state`() = runTest {
    // Given
    every { retrieveUseCase.getRaces() } returns
      flowOf(Result.failure(NetworkException(NetworkExceptionType.NoInternet)))

    val viewModel =
      DashboardViewModel(
        retrieveUseCase,
        pollingUseCase,
        uiMapper,
        networkMonitor,
        categoryUiMapper,
        testDispatcher,
        logger)

    viewModel.state.test {
      assertTrue(awaitItem() is UiState.Loading)
      assertTrue(awaitItem() is UiState.Error)
    }
  }

  @Test
  fun `Given Timeout exception when fetching data then emit Error state`() = runTest {
    every { retrieveUseCase.getRaces() } returns
      flowOf(Result.failure(NetworkException(NetworkExceptionType.Timeout)))
    val viewModel =
      DashboardViewModel(
        retrieveUseCase,
        pollingUseCase,
        uiMapper,
        networkMonitor,
        categoryUiMapper,
        testDispatcher,
        logger)

    viewModel.state.test {
      assertTrue(awaitItem() is UiState.Loading)
      assertTrue(awaitItem() is UiState.Error)
    }
  }

  @Test
  fun `Given UnknownHost exception when fetching data then emit Error state`() = runTest {
    every { retrieveUseCase.getRaces() } returns
      flowOf(Result.failure(NetworkException(NetworkExceptionType.UnknownHost)))

    val viewModel =
      DashboardViewModel(
        retrieveUseCase,
        pollingUseCase,
        uiMapper,
        networkMonitor,
        categoryUiMapper,
        testDispatcher,
        logger)

    viewModel.state.test {
      assertTrue(awaitItem() is UiState.Loading)
      assertTrue(awaitItem() is UiState.Error)
    }
  }

  @Test
  fun `Given ConnectionError exception when fetching data then emit Error state`() = runTest {
    every { retrieveUseCase.getRaces() } returns
      flowOf(Result.failure(NetworkException(NetworkExceptionType.ConnectionError)))
    val viewModel =
      DashboardViewModel(
        retrieveUseCase,
        pollingUseCase,
        uiMapper,
        networkMonitor,
        categoryUiMapper,
        testDispatcher,
        logger)

    viewModel.state.test {
      assertTrue(awaitItem() is UiState.Loading)
      assertTrue(awaitItem() is UiState.Error)
    }
  }

  @Test
  fun `Given ServerError exception when fetching data then emit Error state`() = runTest {
    // Given
    every { retrieveUseCase.getRaces() } returns
      flowOf(Result.failure(NetworkException(NetworkExceptionType.ServerError(500))))
    val viewModel =
      DashboardViewModel(
        retrieveUseCase,
        pollingUseCase,
        uiMapper,
        networkMonitor,
        categoryUiMapper,
        testDispatcher,
        logger)

    viewModel.state.test {
      assertTrue(awaitItem() is UiState.Loading)
      assertTrue(awaitItem() is UiState.Error)
    }
  }

  @Test
  fun `Given Unknown exception when fetching data then emit Error state`() = runTest {
    every { retrieveUseCase.getRaces() } returns
      flowOf(Result.failure(NetworkException(NetworkExceptionType.Cancelled)))

    val viewModel =
      DashboardViewModel(
        retrieveUseCase,
        pollingUseCase,
        uiMapper,
        networkMonitor,
        categoryUiMapper,
        testDispatcher,
        logger)
    viewModel.state.test {
      assertTrue(awaitItem() is UiState.Loading)
      assertTrue(awaitItem() is UiState.Error)
    }
  }

  @Test
  fun `verify initial state is loading then success with limited races`() = runTest {
    val testRaceDetails = mockRaceDetailsGenerator()

    every { retrieveUseCase.getRaces() } returns flow { emit(Result.success(testRaceDetails)) }

    viewModel =
      DashboardViewModel(
        retrieveUseCase,
        pollingUseCase,
        uiMapper,
        networkMonitor,
        categoryUiMapper,
        testDispatcher,
        logger)

    viewModel.state.test {
      assert(awaitItem() is UiState.Loading)

      val successState = awaitItem()
      assert(successState is UiState.Success)
      successState as UiState.Success

      assertEquals(successState.data.size, 5)
    }
  }

  @Test
  fun `toggle off horse category filters races correctly Harness and Greyhound`() = runTest {
    val selectedCategories = setOf(testCategories[1], testCategories[2])

    every { retrieveUseCase.getRaces() } returns flow { emit(Result.success(testRaceDetails)) }

    viewModel =
      DashboardViewModel(
        retrieveUseCase,
        pollingUseCase,
        uiMapper,
        networkMonitor,
        categoryUiMapper,
        testDispatcher,
        logger)

    viewModel.toggleCategory(testCategories.first()) // toggle off

    viewModel.state.test(timeout = 10.seconds) {
      awaitItem()

      val successState = awaitItem()
      assert(successState is UiState.Success)
      successState as UiState.Success

      assert(successState.data.all { it.categoryId in selectedCategories.map { it.id } })
      assertEquals(successState.data.size, 5)
    }
  }

  @Test
  fun `when no category is selected, default to showing all three categories`() = runTest {
    every { retrieveUseCase.getRaces() } returns flow { emit(Result.success(testRaceDetails)) }

    viewModel =
      DashboardViewModel(
        retrieveUseCase,
        pollingUseCase,
        uiMapper,
        networkMonitor,
        categoryUiMapper,
        testDispatcher,
        logger)

    viewModel.state.test(timeout = 15.seconds) {
      assert(awaitItem() is UiState.Loading)

      val successState = awaitItem()
      assert(successState is UiState.Success)
      successState as UiState.Success

      val expectedCategoryIds = testCategories.map { it.id }
      val resultCategoryIds = successState.data.map { it.categoryId }

      assert(resultCategoryIds.containsAll(expectedCategoryIds))
      assertEquals(successState.data.size, 5)
    }
  }

  @Test
  fun `toggle Category Harness and Greyhound filters races correctly for Horse`() = runTest {
    val targetCategory = testCategories.first()
    val testRaceDetails = testRaceDetails

    every { retrieveUseCase.getRaces() } returns flow { emit(Result.success(testRaceDetails)) }

    viewModel =
      DashboardViewModel(
        retrieveUseCase,
        pollingUseCase,
        uiMapper,
        networkMonitor,
        categoryUiMapper,
        testDispatcher,
        logger)

    viewModel.toggleCategory(testCategories.last()) // toggle off
    viewModel.toggleCategory(testCategories[1]) // toggle off 2nd

    viewModel.state.test {
      awaitItem()

      val successState = awaitItem()
      assert(successState is UiState.Success)
      successState as UiState.Success

      assert(successState.data.all { it.categoryId == targetCategory.id })
      assertEquals(successState.data.size, 3)
    }
  }
}
