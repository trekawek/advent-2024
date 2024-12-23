package day16

import day16.Field.Companion.charFieldToString
import day16.Field.Companion.clone

fun main() {
  val field = Field.readField()
  val scores = Field.newDirectionField(field.width, field.height)
  val start = field.find('S')
  val bestPaths = findBestPaths(field, scores, start, Direction.RIGHT)
  println(
      bestPaths
          .asSequence()
          .map { it.second }
          .flatMap {
            println(it.charFieldToString())
            it.map { p, c -> if (c == 'O') p else null }
          }
          .filterNotNull()
          .toSet()
          .count() + 1)
}

fun findBestPaths(
    field: Field<Char>,
    scores: Field<MutableMap<Direction, Int>>,
    p: Position,
    d: Direction,
    score: Int = 0
): List<Pair<Int, Field<Char>>> {
  if ((scores[p][d] ?: Int.MAX_VALUE) < score) {
    return listOf()
  }
  scores[p][d] = score

  if (field[p] == 'E') {
    return listOf(Pair(score, field.clone()))
  }
  val options =
      listOf(
          Pair(d, 0),
          Pair(d.next(), 1000),
          Pair(d.prev(), 1000),
      )
  val allResults =
      options.flatMap { (d2, s) ->
        val q = p + d2
        if (!field.isValidPosition(q) || field[q] !in setOf('.', 'E')) {
          return@flatMap listOf()
        }
        val v = field[p]
        field[p] = 'O'
        val results = findBestPaths(field, scores, q, d2, score + s + 1)
        field[p] = v
        results
      }
  val minScore = allResults.minOfOrNull { it.first } ?: return listOf()
  return allResults.filter { it.first == minScore }
}
