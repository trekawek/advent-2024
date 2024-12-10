package day10

fun main() {
    val field = readField()
    println(findTrailheads(field).sumOf { getReachable9sBfs(field, it).count() })
}
