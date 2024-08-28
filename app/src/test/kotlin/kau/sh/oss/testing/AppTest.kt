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
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class AppTest {

  @RegisterExtension
  val testRule = CoroutineTestRule()

  @Test
  fun appHasAGreeting() {
    val app = App()
    assertThat(app.greeting).isEqualTo("Coroutine Testing examples - see tests")
  }

  @DisplayName("testing first item emitted")
  @Test
  fun test1() = runTest {
    val app = App(this)
    val items = mutableListOf<Int>()
    launch { app.flow.collect { items.add(it) } }

    app.start()
    runCurrent()

    assertThat(items).contains(1)
  }

  @DisplayName("testing item emitted after delay")
  @Test
  fun test2() = runTest {
    val app = App()
    val list = mutableListOf<Int>()

    backgroundScope.launch {
      app.flow.collect { list.add(it) }

      runCurrent()
      assertThat(list.size).isEqualTo(1)
      assertThat(list[0]).isEqualTo(1)
      advanceTimeBy((3.1).seconds)
      assertThat(list.size).isEqualTo(2)
      assertThat(list[0]).isEqualTo(10)
    }
  }
}
