package day09

fun main() {
    val mapLine = readln()
    val lengths = parseLengths(mapLine)
    val map = parseMap(mapLine)
    println(compact(map, lengths).let(::checksum))
}

private fun parseLengths(mapLine: String) = mapLine.toList().mapIndexed { i, l ->
    if (i % 2 == 0) l.toString().toInt() else null
}.filterNotNull()

private fun compact(map: List<Int?>, lengths: List<Int>): List<Int?> {
    val result = map.toMutableList()
    for (fileId in lengths.indices.reversed()) {
        val length = lengths[fileId]
        val filePosition = result.indexOf(fileId)
        val newPosition = findSpace(result.toList(), length)
        if (newPosition != null && newPosition < filePosition) {
            for (i in (0..<length)) {
                result[newPosition + i] = fileId
                result[filePosition + i] = null
            }
        }
    }
    return result.toList()
}

private fun findSpace(map: List<Int?>, length: Int): Int? {
    var start: Int? = null
    for (i in map.indices) {
        if (map[i] == null) {
            if (start == null) {
                start = i
            }
            if (i - start + 1 >= length) {
                return start
            }
        } else {
            start = null
        }
    }
    return null
}
