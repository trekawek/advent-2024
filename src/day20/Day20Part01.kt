package day20

import java.util.LinkedList
import java.util.Queue
import kotlin.math.abs

fun main() {
    val field = Field.readField()
    val start = field.find('S')
    val end = field.find('E')
    println(countCheats(field, start, end, 2, 100))
}

fun countCheats(field: Field<Char>, start: Position, end: Position, maxCheatLength: Int, minSave: Int): Int {
    val baseScore = findScoreBfs(field, start, end, null)
    val maxScore = baseScore - minSave
    val results = mutableMapOf<Int, MutableList<Pair<Position, Position>>>()
    for (i in (0..<field.width)) {
        for (j in (0..<field.height)) {
            val p = Pair(i, j)
            if (field[p] == '#') {
                continue
            }
            for (k in (0..<field.width)) {
                for (l in (0..<field.height)) {
                    val q = Pair(k, l)
                    if (field[q] == '#') {
                        continue
                    }
                    if (p == q) {
                        continue
                    }
                    val cheat = Pair(p, q)
                    val dist = dist(cheat)
                    if (dist !in (2..maxCheatLength)) {
                        continue
                    }
                    println(cheat)
                    val score = findScoreBfs(field, start, end, cheat, maxScore)
                    if (score <= maxScore) {
                        val gain = baseScore - score
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
    println(grouped.joinToString("\n"))
    return grouped.sumOf { it.first }
}

fun findScoreBfs(
    field: Field<Char>,
    start: Position,
    end: Position,
    cheat: Pair<Position, Position>?,
    maxScore: Int = Int.MAX_VALUE
): Int {
    val queue: Queue<Entry> = LinkedList()
    queue.add(Entry(start, 0))

    val dist = cheat?.let(::dist) ?: Int.MAX_VALUE
    val scores = Field.newScoreField(field.width, field.height)
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
        if (p == end) {
            continue
        }
        if (cheat != null) {
            if (p == cheat.first) {
                queue += Entry(cheat.second, score + dist)
                continue
            }
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
    return scores[end]
}

data class Entry(val position: Position, val score: Int)

fun dist(pair: Pair<Position, Position>): Int {
    return abs(pair.first.first - pair.second.first) + abs(pair.first.second - pair.second.second)
}