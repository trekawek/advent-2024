package day12

fun main() {
  val field: List<List<Char?>> = getField()
  val visited: List<MutableList<Boolean>> =
      field.indices.map { field[0].indices.map { false }.toMutableList() }
  val result =
      field.indices
          .flatMap { i ->
            field[i].indices.mapNotNull { j ->
              if (visited[i][j]) {
                null
              } else {
                findAreaAndPerimeter(field, visited, Pair(i, j))
              }
            }
          }
          .sumOf { it.area * it.perimeter }
  println(result)
}

fun getField(): List<List<Char?>> =
    generateSequence(::readlnOrNull).map { it.map { e -> e as Char? }.toList() }.toList()

private fun findAreaAndPerimeter(
    field: List<List<Char?>>,
    visited: List<MutableList<Boolean>>,
    p: Position
): AreaAndPerimeter {
  var result = AreaAndPerimeter(1, 0)

  val v = field[p.first][p.second]
  visited[p.first][p.second] = true

  for (d in Direction.entries) {
    val q = p + d
    if (!inField(field, q)) {
      result = result.addPerimeter(1)
      continue
    }
    val w = field[q.first][q.second]
    if (w != v) {
      result = result.addPerimeter(1)
      continue
    }
    if (visited[q.first][q.second]) {
      continue
    }
    result += findAreaAndPerimeter(field, visited, q)
  }

  return result
}

fun inField(field: List<List<Char?>>, p: Position) =
    p.first >= 0 && p.second >= 0 && p.first < field.size && p.second < field[p.first].size

operator fun Pair<Int, Int>.plus(p: Pair<Int, Int>): Pair<Int, Int> =
    Pair(first + p.first, second + p.second)

data class AreaAndPerimeter(val area: Int, val perimeter: Int) {

  operator fun plus(ap: AreaAndPerimeter) =
      AreaAndPerimeter(area + ap.area, perimeter + ap.perimeter)

  fun addPerimeter(perimeter: Int) = AreaAndPerimeter(area, this.perimeter + perimeter)
}
