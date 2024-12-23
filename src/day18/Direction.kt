package day18

enum class Direction(val offset: Pair<Int, Int>) {
  DOWN(Pair(0, 1)),
  RIGHT(Pair(1, 0)),
  UP(Pair(0, -1)),
  LEFT(Pair(-1, 0))
}
