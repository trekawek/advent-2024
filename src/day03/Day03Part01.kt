package day03

private val pattern = Regex("mul\\((?<arg1>\\d{1,3}),(?<arg2>\\d{1,3})\\)")

fun main() {
    println(generateSequence(::readlnOrNull).toList().flatMap(::parse).sumOf { it.first * it.second })
}

private fun parse(line: String): Sequence<Pair<Int, Int>> = pattern.findAll(line).map {
    Pair(it.groupValues[1].toInt(), it.groupValues[2].toInt())
}