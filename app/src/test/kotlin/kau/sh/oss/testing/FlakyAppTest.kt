/*
 * This source file was generated by the Gradle 'init' task
 */
package kau.sh.oss.testing

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class FlakyAppTest {

  @RegisterExtension
  val testRule = CoroutineTestRule()

  @DisplayName("test 1 - item emit with delay")
  @Test
  fun test1() = runTest { // @TestScope (StandardTestDispatcher)
    val app = App()
    app.stateFlow.test { // @turbineScope (UnconfinedTestDispatcher)
      app.start()  // App's scope.launch (@Dispatchers.IO)
      assertThat(awaitItem()).isEqualTo(0)
      assertThat(awaitItem()).isEqualTo(1)
      assertThat(awaitItem()).isEqualTo(10)
      cancelAndIgnoreRemainingEvents()
    }
  }

  @DisplayName("test 2 - item emit with delay - turbineScope")
  @Test
  fun test2() = runTest {
    turbineScope {
      val app = App()
      val items = app.stateFlow.testIn(this)
      app.start()
      assertThat(items.awaitItem()).isEqualTo(0)
      assertThat(items.awaitItem()).isEqualTo(1)
      advanceTimeBy(3.seconds) // ❌ no effect on test
      assertThat(items.awaitItem()).isEqualTo(10)
      items.cancelAndIgnoreRemainingEvents()
    }
  }

}
