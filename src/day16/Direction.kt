package day16

enum class Direction(val offset: Pair<Int, Int>, private val c: Char) {
  UP(Pair(0, -1), '^'),
  RIGHT(Pair(1, 0), '>'),
  DOWN(Pair(0, 1), 'v'),
  LEFT(Pair(-1, 0), '<');

  fun prev() = entries[(this.ordinal - 1).mod(entries.size)]

  fun next() = entries[(this.ordinal + 1).mod(entries.size)]

  fun toChar() = c
}
