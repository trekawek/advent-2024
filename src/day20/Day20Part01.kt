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
    val results = mutableMapOf<Int, MutableList<Cheat>>()
    for (i in (0..<field.width)) {
        for (j in (0..<field.height)) {
            val p = Position(i, j)
            if (field[p] == '#') {
                continue
            }
            println(p)
            for (k in (0..<field.width)) {
                for (l in (0..<field.height)) {
                    val q = Position(k, l)
                    if (field[q] == '#') {
                        continue
                    }
                    if (p == q) {
                        continue
                    }
                    val cheat = Cheat(p, q)
                    if (cheat.dist() !in (2..maxCheatLength)) {
                        continue
                    }
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
    field: Field<Char>, start: Position, end: Position, cheat: Cheat?, maxScore: Int = Int.MAX_VALUE
): Int {
    val queue: Queue<Entry> = LinkedList()
    queue.add(Entry(start, 0))

    val cheatDist = cheat?.dist() ?: Int.MAX_VALUE
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
        if (p == cheat?.from) {
            queue += Entry(cheat.to, score + cheatDist)
            //addScoresToPaths(scores, cheat, score)
            continue
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

fun addScoresToPaths(scores: Field<Int>, cheat: Cheat, score: Int) {
    val (from, to) = cheat
    val distI = to.i - from.i
    val distJ = to.j - from.j
    val iSeq = if (distI > 0) (from.i..to.i) else (from.i downTo to.i)
    val jSeq = if (distJ > 0) (from.j..to.j) else (from.j downTo to.j)
    for (i in iSeq) {
        val step = abs(i - cheat.from.i)
        scores[Position(i, from.j)] = score + step
        scores[Position(i, to.j)] = score + step + abs(distJ)
    }
    for (j in jSeq) {
        val step = abs(j - cheat.from.j)
        scores[Position(from.i, j)] = score + step
        scores[Position(to.i, j)] = score + step + abs(distI)
    }
}

data class Entry(val position: Position, val score: Int)
