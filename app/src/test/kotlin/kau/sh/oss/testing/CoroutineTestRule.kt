package kau.sh.oss.testing


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

/**
 * Primary rule for Coroutine Tests.
 *
 * @constructor Create empty Test coroutine rule
 *
 * Use it as follows
 *
 * ```kotlin
 * class MyViewModelTest {
 *    @RegisterExtension val testRule = CoroutineTestRule()
 * }
 * ```
 */
@ExperimentalCoroutinesApi
class CoroutineTestRule(
  injectedScheduler: TestCoroutineScheduler? = null,
  injectedDispatcher: TestDispatcher? = null,
) : BeforeEachCallback, AfterEachCallback {

  private val testScheduler: TestCoroutineScheduler = when {
    injectedScheduler != null -> injectedScheduler // respect user instructions first
    injectedDispatcher != null -> injectedDispatcher.scheduler // ensure same scheduler across
    else -> TestCoroutineScheduler()
  }

  private val testDispatcher: TestDispatcher = when {
    injectedDispatcher != null -> injectedDispatcher // respect user instructions first
    else -> StandardTestDispatcher(testScheduler) // ensure same scheduler across
  }


  override fun beforeEach(p0: ExtensionContext?) {
    // ⚠️ Calling this with a TestDispatcher has special behavior:
    // subsequently-called runTest, as well as TestScope and test dispatcher constructors,
    // will use the TestCoroutineScheduler of the provided dispatcher.

    // This means in runTest you don't have to
    Dispatchers.setMain(testDispatcher)
  }

  override fun afterEach(p0: ExtensionContext?) {
    Dispatchers.resetMain()
  }

  fun currentTestTime(): Long {
    return testDispatcher.scheduler.currentTime
  }
}
