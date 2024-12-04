package day04

private val xmas = "XMAS".toList()
fun main() {
    println(generateSequence(::readlnOrNull).toList().map { it.toList() }.let { table ->
        table.indices.flatMap { i ->
            table[i].indices.map { j ->
                findXmas(table, i, j)
            }
        }.sum()
    })
}

// Find all XMAS/SAMX occurrences, starting at (i, j)
private fun findXmas(table: List<List<Char>>, i: Int, j: Int) =
    0 +
            (if (checkHorizontal(table, xmas, i, j)) 1 else 0) +
            (if (checkVertical(table, xmas, i, j)) 1 else 0) +
            (if (checkDiagonal1(table, xmas, i, j)) 1 else 0) +
            (if (checkDiagonal2(table, xmas, i, j)) 1 else 0) +
            (if (checkHorizontal(table, xmas.reversed(), i, j)) 1 else 0) +
            (if (checkVertical(table, xmas.reversed(), i, j)) 1 else 0) +
            (if (checkDiagonal1(table, xmas.reversed(), i, j)) 1 else 0) +
            (if (checkDiagonal2(table, xmas.reversed(), i, j)) 1 else 0)

private fun checkHorizontal(table: List<List<Char>>, word: List<Char>, i: Int, j: Int): Boolean {
    if (word.size > table[0].size - j) {
        return false
    }
    return word.indices.map { k -> table[i][j + k] } == word
}

private fun checkVertical(table: List<List<Char>>, word: List<Char>, i: Int, j: Int): Boolean {
    if (word.size > table.size - i) {
        return false
    }
    return word.indices.map { k -> table[i + k][j] } == word
}

internal fun checkDiagonal1(table: List<List<Char>>, word: List<Char>, i: Int, j: Int): Boolean {
    if (word.size > table.size - i || word.size > table[0].size - j) {
        return false
    }
    return word.indices.map { k -> table[i + k][j + k] } == word
}

internal fun checkDiagonal2(table: List<List<Char>>, word: List<Char>, i: Int, j: Int): Boolean {
    if (word.size > table.size - i || word.size > j + 1) {
        return false
    }
    return word.indices.map { k -> table[i + k][j - k] } == word
}