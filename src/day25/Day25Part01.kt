package day25

fun main() {
  val (keys, locks) = readEntries()

  var fitting = 0
  for (k in keys) {
    for (l in locks) {
      fitting += if (fits(k, l)) 1 else 0
    }
  }
  println(fitting)
}

private fun readEntries(): Pair<List<Key>, List<Lock>> {
  val entries =
      generateSequence(::readlnOrNull).windowed(8, 8, true).map { lines ->
        if (lines.first().toList().all { it == '#' }) {
          parseLock(lines)
        } else {
          parseKey(lines)
        }
      }
  val keys: MutableList<Key> = mutableListOf()
  val locks: MutableList<Lock> = mutableListOf()
  for (e in entries) {
    if (e is Key) {
      keys += e
    }
    if (e is Lock) {
      locks += e
    }
  }
  return Pair(keys, locks)
}

private fun parseKey(lines: List<String>): Key {
  return Key(parseNumbers(lines.filter(String::isNotEmpty).reversed()))
}

private fun parseLock(lines: List<String>): Lock {
  return Lock(parseNumbers(lines.filter(String::isNotEmpty)))
}

private fun parseNumbers(lines: List<String>): List<Int> {
  return lines[0].indices.map { i ->
    lines.mapIndexed { j, line -> if (line[i] == '#') j else null }.filterNotNull().max()
  }
}

private data class Key(val heights: List<Int>)

private data class Lock(val heights: List<Int>)

private fun fits(key: Key, lock: Lock) =
    key.heights.zip(lock.heights).map { it.first + it.second }.all { it < 6 }
