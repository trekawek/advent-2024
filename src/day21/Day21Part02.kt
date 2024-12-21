package day21

fun main() {
    println(generateSequence(::readlnOrNull).sumOf { getComplexity(it, 25) })
}
