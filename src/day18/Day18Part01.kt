package day18

fun main() {
  val bytes = readBytes()
  println(findShortestPath(71, 71, bytes.take(1024)))
}

fun readBytes(): List<Position> {
  return generateSequence(::readlnOrNull)
      .map { it.split(",").map(String::toInt).let { p -> Pair(p[0], p[1]) } }
      .toList()
}

fun findShortestPath(width: Int, height: Int, bytes: List<Position>): Int? {
  val field = Field(width, height) { '.' }
  for (b in bytes) {
    field[b] = '#'
  }

  val scores = Field(field.width, field.height) { Int.MAX_VALUE }
  val start = Pair(0, 0)
  val end = Pair(field.width - 1, field.height - 1)
  findAllPaths(field, scores, start)
  return if (scores[end] == Int.MAX_VALUE) null else scores[end]
}

fun findAllPaths(field: Field<Char>, scores: Field<Int>, p: Position, s: Int = 0) {
  if (scores[p] <= s) {
    return
  }
  scores[p] = s
  for (d in Direction.entries) {
    val q = p + d
    if (!field.isValidPosition(q)) {
      continue
    }
    if (field[q] != '.') {
      continue
    }
    field[q] = 'O'
    findAllPaths(field, scores, q, s + 1)
    field[q] = '.'
  }
}
