package day18

class Field<T>(val width: Int, val height: Int, init: (Position) -> T) {

  private val values =
      (0..<height).map { j -> (0..<width).map { i -> init(Pair(i, j)) }.toMutableList() }

  operator fun get(p: Position): T = values[p.second][p.first]

  operator fun set(p: Position, c: T) {
    values[p.second][p.first] = c
  }

  fun isValidPosition(position: Position) =
      (position.first >= 0 &&
          position.second >= 0 &&
          position.first < width &&
          position.second < height)

  override fun toString(): String {
    return values.joinToString("\n") { it.joinToString("") }
  }
}
