package day06

sealed class AbstractField<E, L : List<E>>(val values: List<L>) {
  private val width: Int
    get() = values[0].size

  private val height: Int
    get() = values.size

  operator fun get(p: Position): E = values[p.second][p.first]

  fun isValidPosition(position: Position) =
      (position.first >= 0 &&
          position.second >= 0 &&
          position.first < width &&
          position.second < height)

  fun find(e: E): Position =
      map { p, v -> if (v == e) p else null }.values.flatMap { it.filterNotNull() }.first()

  fun <F> map(transform: (p: Position, e: E?) -> F?) =
      Field(values.mapIndexed { y, row -> row.mapIndexed { x, e -> transform(Pair(x, y), e) } })

  fun toMutableField() = MutableField(values.map { it.toMutableList() })

  companion object {
    fun readField() =
        generateSequence(::readlnOrNull).map { it.toList() }.toList().let { Field(it) }

    fun <L : List<Char>> AbstractField<Char, L>.findInitialPosition() = find('^')
  }
}

class Field<E>(values: List<List<E>>) : AbstractField<E, List<E>>(values)

class MutableField<E>(values: List<MutableList<E>>) : AbstractField<E, MutableList<E>>(values) {
  operator fun set(p: Position, e: E) {
    values[p.second][p.first] = e
  }
}
