package day06

import day06.AbstractField.Companion.findInitialPosition
import day06.AbstractField.Companion.readField

fun main() {
  val field = readField()
  val (initialWalk, _) = findPath(field)
  val pathsWithLoop =
      initialWalk
          .map { step ->
            val updatedField = field.toMutableField()
            val newPosition = step.p + step.direction
            if (!field.isValidPosition(newPosition)) {
              return@map null
            }
            updatedField[newPosition] = '#'
            val (_, loop) = findPath(updatedField)
            if (loop) newPosition else null
          }
          .filterNotNull()
          .toSet()
          .count()
  println(pathsWithLoop)
}

private fun <L : List<Char>> findPath(field: AbstractField<Char, L>): WalkResult {
  val steps = mutableListOf<Step>()
  var direction = Direction.UP
  var position = field.findInitialPosition()
  val visited = field.map { _, _ -> mutableSetOf<Direction>() }

  while (true) {
    if (!visited[position]!!.add(direction)) {
      return WalkResult(steps, true)
    }
    steps += Step(position, direction)

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
  return WalkResult(steps, false)
}

private data class Step(val p: Position, val direction: Direction)

private data class WalkResult(val steps: List<Step>, val loop: Boolean)
