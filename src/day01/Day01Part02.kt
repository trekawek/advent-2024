package day01

fun main() {
    println(
        generateSequence(::readlnOrNull).toList().map(::parseLine).unzip().run {
            val map1 = count(first)
            val map2 = count(second)
            map1.entries.map { it.key * it.value * (map2[it.key] ?: 0) }
        }.sum()
    )
}

private fun count(list: List<Int>): Map<Int, Int> = list.groupBy { it }.mapValues { it.value.count() }
