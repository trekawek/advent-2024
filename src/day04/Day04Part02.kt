package day04

private val mas = "MAS".toList()
fun main() {
    println(generateSequence(::readlnOrNull).toList().map { it.toList() }.let { table ->
        (1..table.size - 2).flatMap { i ->
            (1..table[i].size - 2).map { j -> findXmas(table, i, j) }
        }.count { it }
    })
}

// Find all MAS/SAM occurrences, with a center at (i, j)
private fun findXmas(table: List<List<Char>>, i: Int, j: Int): Boolean {
    val diag1 = checkDiagonal1(table, mas, i - 1, j - 1) || checkDiagonal1(table, mas.reversed(), i - 1, j - 1)
    val diag2 = checkDiagonal2(table, mas, i - 1, j + 1) || checkDiagonal2(table, mas.reversed(), i - 1, j + 1)
    return diag1 && diag2
}
