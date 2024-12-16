package day16

import java.util.EnumSet

class Field<T>(private val values: Array<Array<T>>) {
    val width: Int
        get() = values[0].size
    val height: Int
        get() = values.size

    operator fun get(p: Position): T = values[p.second][p.first]

    operator fun set(p: Position, c: T) {
        values[p.second][p.first] = c
    }

    fun isValidPosition(position: Position) =
        (position.first >= 0 && position.second >= 0 && position.first < width && position.second < height)

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

        fun newDirectionField(width: Int, height: Int): Field<MutableMap<Direction, Int>> {
            val arr = (0..<height).map {
                (0..<width).map {
                    mutableMapOf<Direction, Int>()
                }.toTypedArray()
            }.toTypedArray()
            return Field(arr)
        }

        fun Field<Char>.clone(): Field<Char> {
            return Field(values.map { it.clone() }.toTypedArray())
        }

        fun Field<Char>.charFieldToString(): String {
            val builder = StringBuilder()
            for (row in values) {
                builder.append(row.joinToString(""))
                builder.append("\n")
            }
            return builder.toString()
        }
    }
}
