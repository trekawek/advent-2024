package day15

fun main() {
  val field = Field.readField()
  val movements =
      generateSequence(::readlnOrNull)
          .flatMap(String::toList)
          .map(Direction::charToDirection)
          .toList()

  var p = field.findInitialPosition()
  for (m in movements) {
    val q = p + m
    if (!field.isValidPosition(q)) {
      continue
    }
    val moved =
        when (val v = field[q]) {
          'O' -> tryToMoveBox(field, q, m)
          '#' -> false
          '.' -> true
          else -> throw IllegalArgumentException("Invalid value: $v")
        }
    if (moved) {
      field[p] = '.'
      field[q] = '@'
      p = q
    }
  }

  println(boxGps(field).sum())
}

private fun boxGps(field: Field) =
    field.map { pos, c -> if (c == 'O') pos.first + pos.second * 100 else null }.filterNotNull()

private fun tryToMoveBox(field: Field, p: Pair<Int, Int>, m: Direction): Boolean {
  var q = p
  while (field[q] == 'O') {
    q += m
    if (!field.isValidPosition(q)) {
      return false
    }
  }
  return if (field[q] == '.') {
    field[q] = 'O'
    field[p] = '.'
    true
  } else {
    false
  }
}
