package day10

fun main() {
    val field = readField()
    println(findTrailheads(field).sumOf { getTrailsCount(field, it) })
}

private fun getTrailsCount(field: List<List<Int>>, position: Position): Int {
    val current = field[position.first][position.second]
    if (current == 9) {
        return 1
    }
    return Direction.entries.sumOf { d ->
        val newPosition = position + d
        if (newPosition.first < 0 || newPosition.second < 0 || newPosition.first >= field.size || newPosition.second >= field[0].size) {
            0
        } else if (field[newPosition.first][newPosition.second] == current + 1) {
            getTrailsCount(field, newPosition)
        } else {
            0
        }
    }
}