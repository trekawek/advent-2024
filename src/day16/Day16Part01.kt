package day16

fun main() {
    val field = Field.readField()
    val scores = Field.newDirectionField(field.width, field.height)
    val start = field.find('S')
    println(findScore(field, scores, start, Direction.RIGHT))
}

fun findScore(
    field: Field<Char>, scores: Field<MutableMap<Direction, Int>>, p: Position, d: Direction, score: Int = 0
): Int? {
    if ((scores[p][d] ?: Int.MAX_VALUE) < score) {
        return null
    }
    scores[p][d] = score

    if (field[p] == 'E') {
        return score
    }
    val options = listOf(
        Pair(d, 0),
        Pair(d.next(), 1000),
        Pair(d.prev(), 1000),
    )
    return options.map { (d2, s) ->
        val q = p + d2
        if (!field.isValidPosition(q) || field[q] !in setOf('.', 'E')) {
            return@map null
        }
        val v = field[p]
        field[p] = d2.toChar()
        val newScore = findScore(field, scores, q, d2, score + s + 1)
        field[p] = v
        newScore
    }.filterNotNull().minOrNull()
}
