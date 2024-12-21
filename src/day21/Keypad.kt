package day21

class Keypad private constructor(
    buttons: String,
    private val delegate: ButtonRequester,
) : ButtonRequester {
    val width: Int
        get() = buttons[0].size
    val height: Int
        get() = buttons.size

    private val buttons: List<List<Char>> =
        buttons.split('\n').map(String::trim).filter(String::isNotBlank).map(String::toList)

    private fun find(button: Char) = buttons.flatMapIndexed { j, row ->
        row.mapIndexed { i, b -> if (b == button) Position(i, j) else null }
    }.filterNotNull().first()

    val cache: MutableMap<Triple<Position, Position, Position?>, Pair<Long, Position>> = mutableMapOf()

    override fun request(buttonList: List<Char>, position: Position?): Pair<Long, Position> {
        var p = position ?: find('A')
        var r: Position? = null
        val result = Pair(buttonList.sumOf { button ->
            val q = find(button)
            val cacheKey = Triple(p, q, r)
            val (cost, newR) = if (cache.contains(cacheKey)) {
                cache[cacheKey]!!
            } else {
                val paths = findMinPaths(findAllPaths(p, q))
                paths.map { path ->
                    delegate.request(path.map { it.toChar() } + 'A', r)
                }.minByOrNull { it.first }!!.also { cache[cacheKey] = it }
            }
            r = newR
            p = q
            cost
        }, p)
        return result
    }

    private fun findMinPaths(paths: List<Path>): List<Path> {
        val min = paths.minOfOrNull { it.size }
        return paths.filter { it.size == min }
    }

    private fun findAllPaths(
        p: Position,
        end: Position,
        path: Path = listOf(),
        scores: MutableMap<Position, Int> = mutableMapOf()
    ): List<Path> {
        if ((scores[p] ?: Int.MAX_VALUE) < path.size) {
            return listOf()
        }
        scores[p] = path.size
        if (p == end) {
            return listOf(path)
        }
        return Direction.entries.flatMap { d ->
            val q = p + d
            if (!isValidPosition(q) || buttons[q.j][q.i] == '-') {
                listOf()
            } else {
                findAllPaths(q, end, path + d, scores)
            }
        }
    }

    fun isValidPosition(position: Position) =
        (position.i >= 0 && position.j >= 0 && position.i < width && position.j < height)

    companion object {
        fun newNumericKeypad(delegate: ButtonRequester): Keypad {
            return Keypad(
                """
                789
                456
                123
                -0A
            """.trimIndent(), delegate
            )
        }

        fun newDirectionalKeypad(
            delegate: ButtonRequester,
        ): Keypad {
            return Keypad(
                """
                -^A
                <v>
            """.trimIndent(), delegate
            )
        }
    }
}