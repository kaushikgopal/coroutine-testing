package kau.sh.oss.testing

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.annotations.VisibleForTesting
import kotlin.time.Duration.Companion.seconds

class Cache(
  scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {
  @VisibleForTesting
  val cache = mutableListOf<Int>()

  @VisibleForTesting
  val extendedCache = mutableListOf<Int>()

  fun put(value: Int) {
    val cacheSize = cache.size

    if (cacheSize > 0) {
      if (extendedCache.size == 5) extendedCache.dropLast(cacheSize)
      extendedCache.addAll(cache)
    }
    cache.add(value)
  }


  init {
    scope.launch {
      while (true) {
          delay(5.seconds)
        extendedCache.clear()

        cache.forEach { extendedCache.add(it) }
        cache.clear()
      }
    }
  }


}
