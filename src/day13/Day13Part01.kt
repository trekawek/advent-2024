package day13

fun main() {
    val machines = readMachines()
    println(machines.mapNotNull(LinearEq::solve).filter { it.first <= 100 && it.second <= 100 }
        .sumOf { 3 * it.first + it.second })
}

fun readMachines() = buildList {
    val lines: MutableList<String> = mutableListOf()
    for (line in generateSequence(::readlnOrNull)) {
        if (line.isEmpty()) {
            add(parseMachine(lines))
            lines.clear()
        } else {
            lines += line
        }
    }
    if (lines.isNotEmpty()) {
        add(parseMachine(lines))
    }
}

val buttonPattern = Regex("Button [A-Z]: X\\+(\\d+), Y\\+(\\d+)")
val prizePattern = Regex("Prize: X=(\\d+), Y=(\\d+)")
fun parseMachine(lines: List<String>): LinearEq {
    val columns = mutableListOf<List<Long>>()
    var constants: List<Long>? = null
    for (l in lines) {
        buttonPattern.matchEntire(l)?.let { columns += it.groupValues.subList(1, 3).map(String::toLong) }
        prizePattern.matchEntire(l)?.let { constants = it.groupValues.subList(1, 3).map(String::toLong) }
    }
    return LinearEq(columns, constants!!)
}

fun List<List<Long>>.det(): Long {
    return this[0][0] * this[1][1] - this[1][0] * this[0][1]
}

data class LinearEq(
    private val columns: List<List<Long>>,
    private val constants: List<Long>,
) {
    fun solve(): Pair<Long, Long>? {
        val d = columns.det()
        if (d == 0L) {
            return null
        }
        val dA = columns.toMutableList().apply { this[0] = constants }.det()
        val dB = columns.toMutableList().apply { this[1] = constants }.det()
        if (dA % d != 0L || dB % d != 0L) {
            return null
        }
        return Pair(dA / d, dB / d)
    }

    fun mapConstants(transform: (Long) -> Long) = LinearEq(columns, constants.map(transform))
}

