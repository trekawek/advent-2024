package day20

import kotlin.math.abs

data class Cheat(val from: Position, val to: Position) {
    fun dist(): Int {
        return abs(from.i - to.i) + abs(from.j - to.j)
    }
}
