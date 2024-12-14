package day14

const val SECONDS = 100
const val WIDTH = 101
const val HEIGHT = 103
fun main() {
    println(generateSequence(::readlnOrNull).map(::parseRobot).toList().let { countQuadrants(it) }
        .filterKeys { it != -1 }.values.reduce { i, j -> i * j })
}

private fun countQuadrants(robots: List<Robot>): Map<Int, Int> =
    robots.map { it.fastForward(SECONDS).p }.groupBy(::getQuadrant).mapValues { it.value.size }

private fun getQuadrant(p: Pair<Int, Int>): Int {
    val halfWidth = WIDTH / 2
    val halfHeight = HEIGHT / 2
    return when {
        p.first < halfWidth && p.second < halfHeight -> 0
        p.first > halfWidth && p.second < halfHeight -> 1
        p.first > halfWidth && p.second > halfHeight -> 2
        p.first < halfWidth && p.second > halfHeight -> 3
        else -> -1
    }
}

fun parseRobot(line: String): Robot {
    val values = line.split(' ').map(::parseTriple).associateBy { it.first }
    return Robot(values["p"].let { Pair(it!!.second, it.third) }, values["v"].let { Pair(it!!.second, it.third) })
}

private fun parseTriple(s: String) = s.split('=').let {
    val name = it[0]
    val values = it[1].split(',').map(String::toInt)
    Triple(name, values[0], values[1])
}

data class Robot(val p: Pair<Int, Int>, val v: Pair<Int, Int>) {
    fun fastForward(s: Int): Robot {
        return Robot(Pair((p.first + v.first * s).mod(WIDTH), (p.second + v.second * s).mod(HEIGHT)), v)
    }
}