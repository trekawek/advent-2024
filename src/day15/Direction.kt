package day15

enum class Direction(val offset: Pair<Int, Int>) {
    UP(Pair(0, -1)), RIGHT(Pair(1, 0)), DOWN(Pair(0, 1)), LEFT(Pair(-1, 0));

    companion object {
        fun charToDirection(c: Char): Direction {
            return when (c) {
                '^' -> UP
                '>' -> RIGHT
                'v' -> DOWN
                '<' -> LEFT
                else -> throw IllegalArgumentException("Not a direction char: $c")
            }
        }
    }
}
