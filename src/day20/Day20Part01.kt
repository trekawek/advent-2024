package day20

import java.util.LinkedList
import java.util.Queue

private const val MAX_CHEATS = 2

fun main() {
    val field = Field.readField()
    val start = field.find('S')
    part01Bfs(field, start)
}

fun part01Bfs(field: Field<Char>, start: Position) {
    val noCheat = findScoreBfs(field, start, false).first().score
    val cheatsToPico =
        findScoreBfs(field, start, true, noCheat - 100).groupBy { it.score }.mapKeys { noCheat - it.key }
            .filterKeys { it > 0 }.mapValues { it.value.distinctBy { result -> result.cheats.start }.count() }
            .map { Pair(it.value, it.key) }.sortedBy { it.second }
    println(cheatsToPico.joinToString("\n"))
    println(cheatsToPico.filter { it.second >= 100 }.sumOf { it.first })
}

fun findScoreBfs(field: Field<Char>, start: Position, cheatsEnabled: Boolean, maxScore: Int = Int.MAX_VALUE): List<Result> {
    val queue: Queue<Entry> = LinkedList()
    queue.add(Entry(start, CheatState(cheatsEnabled), 0))

    val scores = Field.newScoreField(field.width, field.height)
    val results = mutableListOf<Result>()
    while (queue.isNotEmpty()) {
        val (p, cheatState, score) = queue.remove()
        if (score > maxScore) {
            continue
        }

        val updatedCheatState = if (cheatState.started) {
            cheatState.decreaseRemaining(p)
        } else {
            cheatState
        }

        val cheatToScore = scores[p]
        val memoized = cheatToScore[updatedCheatState] ?: Int.MAX_VALUE
        if (memoized <= score) {
            continue
        } else {
            cheatToScore[updatedCheatState] = score
        }

        if (field[p] == 'E') {
            if (updatedCheatState.started && updatedCheatState.available) {
                continue
            }
            results += Result(score, updatedCheatState)
            continue
        }

        for (d in Direction.entries) {
            val q = p + d
            if (!field.isValidPosition(q)) {
                continue
            }

            val w = field[q]
            if (w == '#' && !updatedCheatState.available) {
                continue
            }

            val startedCheatState = if (w == '#' && !updatedCheatState.started) {
                updatedCheatState.start(p)
            } else {
                updatedCheatState
            }

            queue += Entry(q, startedCheatState, score + 1)
        }
    }
    return results
}

data class Entry(val position: Position, val cheats: CheatState, val score: Int)

data class Result(val score: Int, val cheats: CheatState)

data class CheatState(
    val enabled: Boolean,
    val started: Boolean = false,
    val remaining: Int = MAX_CHEATS,
    val start: Position? = null,
    val end: Position? = null,
) {
    val available: Boolean
        get() = enabled && remaining > 0

    fun decreaseRemaining(position: Position): CheatState {
        if (remaining == 0) {
            return this
        }
        val newRemaining = remaining - 1
        val newEnd = if (newRemaining == 0) {
            position
        } else {
            end
        }
        return CheatState(enabled, started, newRemaining, start, newEnd)
    }

    fun start(position: Position) = CheatState(enabled, true, remaining - 1, position, end)
}