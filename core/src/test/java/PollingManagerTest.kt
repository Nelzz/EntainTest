/* (C) 2025. All rights reserved. */
import com.racingapp.core.PollingManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PollingManagerTest {

  private lateinit var pollingManager: PollingManager
  private val testScope = TestScope()
  private val testDispatcher = StandardTestDispatcher(testScope.testScheduler)

  @Before
  fun setUp() {
    Dispatchers.setMain(testDispatcher)
    pollingManager = PollingManager(testScope)
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun `startPolling should invoke fetchData at expected intervals`() = runTest {
    val fetchData = mockk<suspend () -> Unit>(relaxed = true)
    val intervalMillis = 1000L

    pollingManager.startPolling(intervalMillis, fetchData)

    advanceTimeBy(intervalMillis)
    coVerify(exactly = 1) { fetchData() }

    advanceTimeBy(intervalMillis)
    coVerify(exactly = 2) { fetchData() }

    pollingManager.stopPolling()
  }

  @Test
  fun `stopPolling should cancel polling`() = runTest {
    val fetchData = mockk<suspend () -> Unit>(relaxed = true)
    val intervalMillis = 1000L

    pollingManager.startPolling(intervalMillis, fetchData)
    advanceTimeBy(intervalMillis * 2)
    coVerify(exactly = 2) { fetchData() }

    pollingManager.stopPolling()
    advanceTimeBy(intervalMillis * 2)
    coVerify(exactly = 2) { fetchData() }
  }

  @Test
  fun `fetchData should timeout if execution exceeds timeoutMillis`() = runTest {
    val timeoutMillis = 500L
    pollingManager = PollingManager(testScope, timeoutMillis)

    val fetchData = mockk<suspend () -> Unit>()

    coEvery { fetchData() } coAnswers { delay(timeoutMillis + 100) }

    pollingManager.startPolling(1000L, fetchData)
    advanceTimeBy(2000L)

    coVerify(exactly = 2) { fetchData() }
    pollingManager.stopPolling()
  }
}
