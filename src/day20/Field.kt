package day20

import java.util.EnumSet

class Field<T>(private val values: Array<Array<T>>) {
    val width: Int
        get() = values[0].size
    val height: Int
        get() = values.size

    operator fun get(p: Position): T = values[p.j][p.i]

    operator fun set(p: Position, c: T) {
        values[p.j][p.i] = c
    }

    fun isValidPosition(position: Position) =
        (position.i >= 0 && position.j >= 0 && position.i < width && position.j < height)

    fun <S> map(transform: (Position, T) -> S): List<S> = values.flatMapIndexed { j, row ->
        row.mapIndexed { i, c -> transform(Position(i, j), c) }
    }

    fun find(e: T) = map { p, c -> if (c == e) p else null }.filterNotNull().first()

    override fun toString(): String {
        return values.joinToString("\n") {
            it.joinToString(", ")
        }
    }

    companion object {
        fun readField() =
            generateSequence(::readlnOrNull).takeWhile { it.isNotBlank() }.map { it.toList().toTypedArray() }.toList()
                .toTypedArray().let { Field(it) }

        fun newScoreField(width: Int, height: Int): Field<Int> {
            val arr = (0..<height).map {
                (0..<width).map {
                    Int.MAX_VALUE
                }.toTypedArray()
            }.toTypedArray()
            return Field(arr)
        }

        fun Field<Char>.clone(): Field<Char> {
            return Field(values.map { it.clone() }.toTypedArray())
        }

        fun Field<Char>.charFieldToString(marks: Map<Position, Char> = mapOf()): String {
            val builder = StringBuilder()
            for (j in values.indices) {
                for (i in values[j].indices) {
                    val p = Position(i, j)
                    builder.append(marks[p] ?: this[p])
                }
                builder.append("\n")
            }
            return builder.toString()
        }
    }
}
