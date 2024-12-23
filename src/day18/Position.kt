package day18

typealias Position = Pair<Int, Int>

internal operator fun Position.plus(d: Direction): Position {
  return Pair(this.first + d.offset.first, this.second + d.offset.second)
}
