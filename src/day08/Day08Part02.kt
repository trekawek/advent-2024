package day08

import java.lang.Integer.min

fun main() {
  val field = generateSequence(::readlnOrNull).map { it.toList() }.toList()
  val size = Point(field.size, field[0].size)
  val antennas =
      findAntennas(field)
          .flatMap { getAntinodes(it.value, size) }
          .toSet()
          .toList()
          .sortedBy { it.first }
  println(antennas.size)
}

private fun getAntinodes(antennas: List<Point>, size: Point) =
    antennas.indices.flatMap { i ->
      antennas.indices
          .filter { j -> i != j }
          .flatMap { j ->
            val p = antennas[i]
            val q = antennas[j]
            getAntinodes(p, q, size)
          }
    }

private fun getAntinodes(p: Point, q: Point, size: Point): List<Point> {
  val v = q - p
  val from = -div(p, -v, size)
  val to = div(p, v, size)
  return (from..to).map { i -> p + v * i }
}

// How many times vector v can be added to p before it goes out of the map
private fun div(p: Point, v: Vector, size: Point): Int {
  val i =
      if (v.first < 0) {
        p.first / -v.first
      } else {
        ((size.first - 1) - p.first) / v.first
      }
  val j =
      if (v.second < 0) {
        p.second / -v.second
      } else {
        ((size.second - 1) - p.second) / v.second
      }
  return min(i, j)
}
