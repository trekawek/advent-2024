package day15

class Field(private val values: Array<CharArray>) {
    private val width: Int
        get() = values[0].size
    private val height: Int
        get() = values.size

    operator fun get(p: Position): Char = values[p.second][p.first]

    operator fun set(p: Position, c: Char) {
        values[p.second][p.first] = c
    }

    fun isValidPosition(position: Position) =
        (position.first >= 0 && position.second >= 0 && position.first < width && position.second < height)

    fun findInitialPosition() = find('@')

    fun <T> map(transform: (Position, Char) -> T): List<T> = values.flatMapIndexed { j, row ->
        row.mapIndexed { i, c -> transform(Position(i, j), c) }
    }

    private fun find(e: Char) = map { p, c -> if (c == e) p else null }.filterNotNull().first()

    override fun toString(): String {
        val builder = StringBuilder()
        for (row in values) {
            builder.append(row.joinToString(""))
            builder.append("\n")
        }
        return builder.toString()
    }

    companion object {
        fun readField() =
            generateSequence(::readlnOrNull).takeWhile { it.isNotBlank() }.map { it.toCharArray() }.toList()
                .toTypedArray().let { Field(it) }

        fun readDoubleField() = generateSequence(::readlnOrNull).takeWhile { it.isNotBlank() }.map {
            it.toList().flatMap { c ->
                when (c) {
                    '.' -> ".."
                    '#' -> "##"
                    'O' -> "[]"
                    '@' -> "@."
                    else -> throw IllegalArgumentException("Illegal char: $c")
                }.toList()
            }.toCharArray()
        }.toList().toTypedArray().let { Field(it) }
    }
}
