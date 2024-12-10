package day10

fun main() {
    val field = readField()
    println(findTrailheads(field).sumOf { getReachable9s(field, it).toSet().count() })
}

fun readField() = generateSequence(::readlnOrNull).map { row -> row.toList().map { it.toString().toInt() } }.toList()

fun findTrailheads(field: List<List<Int>>) =
    field.flatMapIndexed { i, row -> row.mapIndexed { j, e -> if (e == 0) Pair(i, j) else null }.filterNotNull() }

private fun getReachable9s(field: List<List<Int>>, position: Position): List<Pair<Int, Int>> {
    val current = field[position.first][position.second]
    if (current == 9) {
        return listOf(position)
    }
    return Direction.entries.flatMap { d ->
        val newPosition = position + d
        if (newPosition.first < 0 || newPosition.second < 0 || newPosition.first >= field.size || newPosition.second >= field[0].size) {
            listOf()
        } else if (field[newPosition.first][newPosition.second] == current + 1) {
            getReachable9s(field, newPosition)
        } else {
            listOf()
        }
    }
}

enum class Direction(val offset: Pair<Int, Int>) {
    UP(Pair(0, -1)), RIGHT(Pair(1, 0)), DOWN(Pair(0, 1)), LEFT(Pair(-1, 0))
}

typealias Position = Pair<Int, Int>

operator fun Position.plus(d: Direction): Position {
    return Pair(this.first + d.offset.first, this.second + d.offset.second)
}

