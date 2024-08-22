/*
 * This source file was generated by the Gradle 'init' task
 */
package kau.sh.oss.testing

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class CoroutineTestRuleTest {

  @Nested
  inner class DefaultDispatcherTest {
    @RegisterExtension
    val testRule = CoroutineTestRule()

    @Test
    @DisplayName("advancement respected by testScope + rule extension")
    fun test1() = runTest {
      assertThat(currentTime).isEqualTo(0.seconds.inWholeMilliseconds)
      assertThat(currentTime).isEqualTo(testRule.currentTestTime())
      advanceTimeBy(2.seconds)
      assertThat(currentTime).isEqualTo(2.seconds.inWholeMilliseconds)
      assertThat(currentTime).isEqualTo(testRule.currentTestTime())
    }
  }


  @Nested
  inner class InjectedSchedulerTest {
    private val customScheduler = TestCoroutineScheduler()

    @RegisterExtension
    val testRule = CoroutineTestRule(injectedScheduler = customScheduler)

    @Test
    @DisplayName("advancement respected by testScope + rule extension")
    fun test1() = runTest {
      assertThat(currentTime).isEqualTo(0.seconds.inWholeMilliseconds)
      assertThat(currentTime).isEqualTo(testRule.currentTestTime())
      advanceTimeBy(2.seconds)
      assertThat(currentTime).isEqualTo(2.seconds.inWholeMilliseconds)
      assertThat(currentTime).isEqualTo(testRule.currentTestTime())
    }
  }


  @Nested
  inner class CustomStandardDispatcherTest {
    private val customDispatcher = StandardTestDispatcher()

    @RegisterExtension
    val testRule = CoroutineTestRule(injectedDispatcher = customDispatcher)

    @Test
    @DisplayName("custom StandardTestDispatcher respects time advancement")
    fun test1() = runTest(customDispatcher) {
      assertThat(0.seconds.inWholeMilliseconds).isEqualTo(currentTime)
      advanceTimeBy(2.seconds)
      assertThat(2.seconds.inWholeMilliseconds).isEqualTo(currentTime)
      assertThat(currentTime).isEqualTo(testRule.currentTestTime())
    }
  }

  @Nested
  inner class CustomUnconfinedDispatcherWithSchedulerTest {
    private val customDispatcher = UnconfinedTestDispatcher()

    @RegisterExtension
    val testRule = CoroutineTestRule(injectedScheduler = customDispatcher.scheduler)

    @Test
    @DisplayName("custom UnconfinedTestDispatcher respects time advancement")
    fun test1() = runTest(customDispatcher) {
      assertThat(0.seconds.inWholeMilliseconds).isEqualTo(currentTime)
      advanceTimeBy(2.seconds)
      // notice that we injected an UnconfinedTestDispatcher
      // but the time is still advanced
      assertThat(2.seconds.inWholeMilliseconds).isEqualTo(currentTime)
      assertThat(currentTime).isEqualTo(testRule.currentTestTime())
    }
  }

  @Nested
  inner class UnconfinedDispatcherTest {
    private val customDispatcher = UnconfinedTestDispatcher()

    @RegisterExtension
    val testRule = CoroutineTestRule(injectedDispatcher = customDispatcher)

    @Test
    @DisplayName("custom UnconfinedTestDispatcher respects time advancement")
    fun test1() = runTest(customDispatcher) {
      assertThat(0.seconds.inWholeMilliseconds).isEqualTo(currentTime)
      advanceTimeBy(2.seconds)
      assertThat(2.seconds.inWholeMilliseconds).isEqualTo(currentTime)
      assertThat(currentTime).isEqualTo(testRule.currentTestTime())
    }
  }

  @Nested
  inner class CustomSchedulerUnconfinedDispatcherTest {
    private val customDispatcher = UnconfinedTestDispatcher()
    private val customScheduler = TestCoroutineScheduler()

    @RegisterExtension
    val testRule = CoroutineTestRule(
        injectedScheduler = customScheduler,
        injectedDispatcher = customDispatcher
    )

    @Test
    @DisplayName("custom UnconfinedTestDispatcher respects time advancement")
    fun test1() = runTest(customDispatcher) {
      assertThat(0.seconds.inWholeMilliseconds).isEqualTo(currentTime)
      advanceTimeBy(2.seconds)
      assertThat(2.seconds.inWholeMilliseconds).isEqualTo(currentTime)
      assertThat(currentTime).isEqualTo(testRule.currentTestTime())
    }
  }
}

