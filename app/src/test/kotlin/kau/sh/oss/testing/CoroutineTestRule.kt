package kau.sh.oss.testing


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.assertj.core.util.VisibleForTesting
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

/**
 * Primary rule for Coroutine Tests. Use it as follows:
 *
 * ```kotlin
 * class MyViewModelTest {
 *    @RegisterExtension val testRule = CoroutineTestRule()
 * }
 * ```
 *
 * @constructor Create empty Test coroutine rule
 * @param injectedDispatcher If dispatcher provided, its scheduler will be prioritized
 * @param injectedScheduler only if dispatcher is not being injected
 *
 */
@ExperimentalCoroutinesApi
class CoroutineTestRule(
  injectedDispatcher: TestDispatcher? = null,
  injectedScheduler: TestCoroutineScheduler? = null,
) : BeforeEachCallback, AfterEachCallback {

  init {
    require(injectedDispatcher == null || injectedScheduler == null) {
      "Cannot provide both a dispatcher and a scheduler"
    }
  }

  @VisibleForTesting
  val testDispatcher: TestDispatcher = when {
    injectedDispatcher != null -> injectedDispatcher
    injectedScheduler != null -> StandardTestDispatcher(injectedScheduler) // if scheduler provided
    else -> StandardTestDispatcher() // ensure same scheduler across
  }

  @VisibleForTesting
  val testScheduler: TestCoroutineScheduler = testDispatcher.scheduler


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
    return testScheduler.currentTime
  }
}
