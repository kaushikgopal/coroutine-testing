/*
 * This source file was generated by the Gradle 'init' task
 */
package kau.sh.oss.testing

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.yield
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class StandardVsUnconfinedTestDispatcherTests {

  @DisplayName("test StandardTestDispatcher - runCurrent")
  @Test
  fun test1() = runTest(StandardTestDispatcher()) {
    var result = "X"

    launch {
      result = "A"
      delay(1.seconds)
      result = "B"
      result = "C"
      delay(2.seconds)
      result = "D"
    }

    runCurrent()
    assertThat(result).isEqualTo("A")
  }

  @DisplayName("test StandardTestDispatcher - advanceTime")
  @Test
  fun test2() = runTest(StandardTestDispatcher()) {
    var result = "X"

    launch {
      result = "A"
      delay(1.seconds)
      result = "B"
      result = "C"
      delay(2.seconds)
      result = "D"
    }

    advanceTimeBy(2.seconds)
    assertThat(result).isEqualTo("C")
  }

  @DisplayName("test StandardTestDispatcher - advanceTime + runCurrent")
  @Test
  fun test3() = runTest(StandardTestDispatcher()) {
    var result = "X"

    launch {
      result = "A"
      delay(1.seconds)
      result = "B"
      result = "C"
      delay(2.seconds)
      result = "D"
    }

    advanceTimeBy(1.seconds)
          // result A is pushed
          // B & C are "scheduled"
    assertThat(result)
        .isEqualTo("A")

    runCurrent()
          // B & C are pushed
          // but no time advancement really
    assertThat(result)
        .isEqualTo("C")

    runCurrent()
          // try that again for safe measure
          // result is same, and no time advancement
    assertThat(result)
        .isEqualTo("C")


    advanceTimeBy(2.seconds)
          // D is scheduled (but not pushed)
    assertThat(result)
        .isEqualTo("C")

    runCurrent()
        // D is now pushed

    assertThat(result).isEqualTo("D")
  }

  @DisplayName("test StandardTestDispatcher - advanceUntilIdle")
  @Test
  fun test4() = runTest(StandardTestDispatcher()) {
    var result = "X"

    launch {
      result = "A"
      delay(1.seconds)
      result = "B"
      result = "C"
      delay(2.seconds)
      result = "D"
    }

    advanceUntilIdle()
    assertThat(result).isEqualTo("D")
  }

  @DisplayName("test UnconfinedTestDispatcher")
  @Test
  fun test5() = runTest(UnconfinedTestDispatcher()) {
    var result = "X"

    val job = launch {
      delay(1.seconds)
      result = "A"
      delay(1.seconds)
      result = "B"
    }

    // advanceTimeBy(1.seconds)
    advanceTimeBy(1.seconds)

    // the job doesn't launch automatically since the coroutine hasn't "started" yet
    assertThat(result).isEqualTo("X")

    // this forces the job to start + complete
    job.join()

    // notice how the result B is ignored
    assertThat(result).isEqualTo("B")
  }

}

