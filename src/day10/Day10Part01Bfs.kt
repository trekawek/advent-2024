package day10

import day06.Position
import java.util.*

fun main() {
  val field = readField()
  println(findTrailheads(field).sumOf { getReachable9sBfs(field, it).toSet().count() })
}

fun getReachable9sBfs(field: List<List<Int>>, startPosition: Position): List<Position> {
  val result = mutableListOf<Position>()
  val queue: Queue<Position> = LinkedList()
  queue += startPosition
  while (queue.isNotEmpty()) {
    val p = queue.remove()
    val v = field[p.first][p.second]
    if (v == 9) {
      result.add(p)
      continue
    }
    for (d in Direction.entries) {
      val q = p + d
      if (q.first in field.indices &&
          q.second in field[q.first].indices &&
          field[q.first][q.second] == v + 1) {
        queue += q
      }
    }
  }
  return result.toList()
}
