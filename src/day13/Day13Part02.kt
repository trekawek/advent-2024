package day13

fun main() {
    val machines = readMachines()
    println(machines.map { it.mapConstants { c -> c + 10000000000000L } }.mapNotNull(LinearEq::solve)
        .sumOf { 3 * it.first + it.second })
}
