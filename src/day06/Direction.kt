package day06

enum class Direction(val offset: Pair<Int, Int>) {
    UP(Pair(0, -1)), RIGHT(Pair(1, 0)), DOWN(Pair(0, 1)), LEFT(Pair(-1, 0))
}
