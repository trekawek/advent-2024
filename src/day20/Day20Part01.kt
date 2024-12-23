package day20

import java.util.LinkedList
import java.util.Queue

fun main() {
    val field = Field.readField()
    val start = field.find('S')
    val end = field.find('E')
    println(countCheats(field, start, end, 2, 100))
}

fun countCheats(field: Field<Char>, start: Position, end: Position, maxCheatLength: Int, minSave: Int): Int {
    val distancesFromStart = findScoreBfs(field, start)
    val results = mutableMapOf<Int, MutableList<Cheat>>()
    for (i in (0..<field.width)) {
        for (j in (0..<field.height)) {
            val p = Position(i, j)
            if (field[p] == '#') {
                continue
            }
            for (k in (0..<field.width)) {
                for (l in (0..<field.height)) {
                    val q = Position(k, l)
                    if (field[q] == '#') {
                        continue
                    }
                    val cheat = Cheat(p, q)
                    if (cheat.dist() !in (2..maxCheatLength)) {
                        continue
                    }
                    val distanceToCheatEnd = distancesFromStart[cheat.from] + cheat.dist()
                    val gain = distancesFromStart[cheat.to] - distanceToCheatEnd
                    if (gain >= minSave) {
                        if (!results.contains(gain)) {
                            results[gain] = mutableListOf()
                        }
                        results[gain]!! += cheat
                    }
                }
            }
        }
    }
    val grouped = results.map { Pair(it.value.size, it.key) }.sortedBy { it.second }
    return grouped.sumOf { it.first }
}

fun findScoreBfs(
    field: Field<Char>,
    start: Position,
    initScore: Int = 0,
    maxScore: Int = Int.MAX_VALUE,
    scores: Field<Int> = Field.newScoreField(field.width, field.height),
): Field<Int> {
    val queue: Queue<Entry> = LinkedList()
    queue.add(Entry(start, initScore))

    while (queue.isNotEmpty()) {
        val (p, score) = queue.remove()
        if (score > maxScore) {
            continue
        }
        if (scores[p] <= score) {
            continue
        } else {
            scores[p] = score
        }
        for (d in Direction.entries) {
            val q = p + d
            if (!field.isValidPosition(q)) {
                continue
            }
            val w = field[p]
            if (w == '#') {
                continue
            }
            queue += Entry(q, score + 1)
        }
    }
    return scores
}

data class Entry(val position: Position, val score: Int)
