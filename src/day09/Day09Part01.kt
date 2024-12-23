package day09

fun main() {
  println(parseMap(readln()).let(::compact).let(::checksum))
}

fun parseMap(mapLine: String) =
    mapLine.toList().flatMapIndexed { i, l ->
      val length = l.toString().toInt()
      if (i % 2 == 0) {
        (0..<length).map { i / 2 }
      } else {
        (0..<length).map { null }
      }
    }

private fun compact(map: List<Int?>): List<Int> {
  val result = map.toMutableList()

  while (true) {
    val firstNull = result.indexOfFirst { it == null }
    val lastElement = result.indexOfLast { it != null }
    if (firstNull < lastElement) {
      result[firstNull] = result[lastElement]
      result[lastElement] = null
    } else {
      break
    }
  }

  return result.toList().filterNotNull()
}

fun checksum(map: List<Int?>) = map.mapIndexed { i, v -> (i * (v ?: 0)).toLong() }.sum()
