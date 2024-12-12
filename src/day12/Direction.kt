package day12

enum class Direction(val offset: Pair<Int, Int>) {
    UP(Pair(-1, 0)), RIGHT(Pair(0, 1)), DOWN(Pair(1, 0)), LEFT(Pair(0, -1))
}
