package day21

enum class Direction(val offset: Pair<Int, Int>, private val c: Char) {
    UP(Pair(0, -1), '^'), RIGHT(Pair(1, 0), '>'), DOWN(Pair(0, 1), 'v'), LEFT(Pair(-1, 0), '<');
    fun toChar() = c
}
