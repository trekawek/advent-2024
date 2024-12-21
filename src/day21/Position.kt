package day21

data class Position(val i: Int, val j: Int) {
    operator fun plus(d: Direction): Position {
        return Position(i + d.offset.first, j + d.offset.second)
    }
}