package day08

fun main() {
    val field = generateSequence(::readlnOrNull).map { it.toList() }.toList()
    val size = Pair(field.size, field[0].size)
    println(findAntennas(field).flatMap { getAntinodes(it.value) }.filter { inField(it, size) }.toSet().count())
}

internal fun findAntennas(field: List<List<Char>>) = field.flatMapIndexed { i, row ->
    row.mapIndexed { j, e ->
        if (e != '.') e to Pair(i, j) else null
    }.filterNotNull()
}.groupBy { it.first }.mapValues { list -> list.value.map { it.second } }

private fun getAntinodes(antennas: List<Point>) = antennas.indices.flatMap { i ->
    antennas.indices.filter { j -> i != j }.flatMap { j ->
        val p = antennas[i]
        val q = antennas[j]
        val v = q - p
        listOf(p - v, q + v)
    }
}

private fun inField(point: Point, size: Point) =
    point.first >= 0 && point.second >= 0 && point.first < size.first && point.second < size.second