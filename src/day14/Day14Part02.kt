package day14

fun main() {
    var robots = generateSequence(::readlnOrNull).map(::parseRobot).take(500).toList()
    var i = 0
    var maxScore: Int? = null
    while (true) {
        val field = toField(robots)
        val score = getChristmasScore(field)
        if (maxScore == null || score > maxScore) {
            println(i)
            printField(field)
            maxScore = score
        }
        robots = robots.map { it.fastForward(1) }
        i++
    }
}

// A single score point is for the robot with a shifted neighbour in the next line, e.g.:
// ...#...
// ..#....
// or
// ...#...
// ....#..
private fun getChristmasScore(field: List<List<Int>>) = field.zipWithNext().sumOf { pair ->
    pair.first.mapIndexed { i, c -> Pair(i, c) }.filter { it.second != 0 }.map { it.first }
        .count { (it > 0 && pair.second[it - 1] != 0) || ((it < pair.second.size - 1) && pair.second[it + 1] != 0) }
}

private fun toField(robots: List<Robot>) = (0..<HEIGHT).map { y ->
    (0..<WIDTH).map { x ->
        countRobots(robots, x, y)
    }
}

private fun printField(field: List<List<Int>>) {
    for (row in field) {
        println(row.joinToString("") { if (it == 0) "." else it.toString() })
    }
}

private fun countRobots(robots: List<Robot>, i: Int, j: Int) = robots.count { it.p.first == i && it.p.second == j }