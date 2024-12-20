package day20

private const val MAX_CHEATS = 2

fun main() {
    val field = Field.readField()
    val start = field.find('S')
    val noCheat = findScore(field, null, start, CheatState(false)).first().score
    val scores = Field.newScoreField(field.width, field.height)
    val cheatsToPico =
        findScore(field, scores, start, CheatState(true), 0).groupBy { it.score }.mapKeys { noCheat - it.key }
            .filterKeys { it > 0 }.mapValues { it.value.distinctBy { result -> result.cheats.start }.count() }
            .map { Pair(it.value, it.key) }.sortedBy { it.second }
    println(cheatsToPico.joinToString("\n"))
    println(cheatsToPico.filter { it.second >= 100 }.sumOf { it.first })
}

fun findScore(
    field: Field<Char>,
    scores: Field<MutableMap<CheatState, Int>>?,
    p: Position,
    cheatState: CheatState,
    score: Int = 0,
): List<Result> {
    val updatedCheatState = if (cheatState.started) {
        cheatState.decreaseRemaining(p)
    } else {
        cheatState
    }

    if (scores != null) {
        val cheatToScore = scores[p]
        val memoized = cheatToScore[updatedCheatState] ?: Int.MAX_VALUE
        if (memoized < score) {
            return listOf()
        } else {
            cheatToScore[updatedCheatState] = score
        }
    }

    if (field[p] == 'E') {
        if (updatedCheatState.started && updatedCheatState.available) {
            return listOf()
        }
        return listOf(Result(score, updatedCheatState))
    }
    return Direction.entries.flatMap { d ->
        val v = field[p]

        val q = p + d
        if (!field.isValidPosition(q)) {
            return@flatMap listOf()
        }

        val w = field[q]
        if (w !in setOf('E', '.', '#')) {
            return@flatMap listOf()
        }
        if (w == '#' && !updatedCheatState.available) {
            return@flatMap listOf()
        }

        val startedCheatState = if (w == '#' && !updatedCheatState.started) {
            updatedCheatState.start(p)
        } else {
            updatedCheatState
        }

        field[p] = 'O'
        val newScore = findScore(field, scores, q, startedCheatState, score + 1)
        field[p] = v
        newScore
    }
}

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