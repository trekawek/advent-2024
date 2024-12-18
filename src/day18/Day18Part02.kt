package day18

fun main() {
    val bytes = readBytes()
    val i = bisect(bytes)
    println(bytes[i])
}

fun bisect(bytes: List<Position>, from: Int = 0, to: Int = bytes.size - 1): Int {
    val middle = (from + to) / 2
    if (middle == from) {
        return to
    }
    return if (pathExists(bytes, middle)) {
        bisect(bytes, middle, to)
    } else {
        bisect(bytes, from, middle)
    }
}

fun pathExists(bytes: List<Position>, index: Int): Boolean {
    return findShortestPath(71, 71, bytes.take(index + 1)) != null
}