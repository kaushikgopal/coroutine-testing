/*
 * This source file was generated by the Gradle 'init' task
 */
package kau.sh.oss.testing

import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class TimingCacheTest {

  @RegisterExtension
  val testRule = CoroutineTestRule()

  @DisplayName("adding an item should immediately put it in the cache")
  @Disabled
  @Test
  fun test1() = runTest {
  }

  @DisplayName("every 5 seconds, an item added to cache, should move to extended cache")
  @Disabled
  @Test
  fun test2() = runTest {
  }

  @DisplayName("every 5 seconds, entire extended cache is cleared")
  @Disabled
  @Test
  fun test3() = runTest {
  }

  @DisplayName("if extended cache already has 5 items, drop oldest item, when adding new item")
  @Disabled
  @Test
  fun test4() = runTest {
  }

}

