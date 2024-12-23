package day06

import day06.AbstractField.Companion.findInitialPosition
import day06.AbstractField.Companion.readField

fun main() {
  val map = readField()
  println(findPath(map))
}

private fun findPath(field: Field<Char>): Int {
  val visited = field.toMutableField()
  var direction = Direction.UP
  var position = field.findInitialPosition()
  var distance = 0

  while (true) {
    if (visited[position] != 'X') {
      visited[position] = 'X'
      distance++
    }

    val newPosition = position + direction
    if (!field.isValidPosition(newPosition)) {
      break
    }
    val destination = field[newPosition]
    if (destination == '#') {
      direction = Direction.entries[(direction.ordinal + 1) % 4]
      continue
    }
    position = newPosition
  }

  return distance
}
