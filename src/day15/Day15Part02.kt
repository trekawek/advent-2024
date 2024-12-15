package day15

import day15.Direction.LEFT
import day15.Direction.RIGHT

fun main() {
    val field = Field.readDoubleField()
    val movements = generateSequence(::readlnOrNull).flatMap(String::toList).map(Direction::charToDirection).toList()

    var p = field.findInitialPosition()
    for (m in movements) {
        val q = p + m
        if (!field.isValidPosition(q)) {
            continue
        }
        val moved = when (field[q]) {
            '[', ']' -> tryToMoveDoubleBox(field, q, m)
            '#' -> false
            '.' -> true
            else -> throw IllegalArgumentException()
        }
        if (moved) {
            field[p] = '.'
            field[q] = '@'
            p = q
        }
    }

    println(doubleBoxGps(field).sum())
}

private fun doubleBoxGps(field: Field) =
    field.map { pos, c -> if (c == '[') pos.first + pos.second * 100 else null }.filterNotNull()

private fun tryToMoveDoubleBox(field: Field, p: Position, d: Direction): Boolean {
    return if (d == LEFT || d == RIGHT) {
        if (canMoveDoubleBoxHorizontally(field, p, d)) {
            moveDoubleBoxHorizontally(field, p, d)
            true
        } else {
            false
        }
    } else {
        if (canMoveDoubleBoxVertically(field, p, d)) {
            moveDoubleBoxVertically(field, p, d)
            true
        } else {
            false
        }
    }
}

private fun canMoveDoubleBoxHorizontally(field: Field, p: Position, d: Direction): Boolean {
    var q = p
    while (field[q] == '[' || field[q] == ']') {
        q += d
        if (!field.isValidPosition(q)) {
            return false
        }
    }
    if (field[q] != '.') {
        return false
    }
    return true
}

private fun moveDoubleBoxHorizontally(field: Field, p: Position, d: Direction) {
    var q = p
    var c = field[q]
    do {
        q += d
        val d = field[q]
        field[q] = c
        c = d
    } while (c != '.')
}

private fun canMoveDoubleBoxVertically(field: Field, p: Position, d: Direction): Boolean {
    return when (val v = field[p]) {
        '[' -> canMoveDoubleBoxVertically(field, p + d, d) && canMoveDoubleBoxVertically(field, p + d + RIGHT, d)

        ']' -> canMoveDoubleBoxVertically(field, p + d, d) && canMoveDoubleBoxVertically(field, p + d + LEFT, d)

        '.' -> true
        '#' -> false
        else -> throw IllegalArgumentException()
    }
}

private fun moveDoubleBoxVertically(field: Field, p: Position, d: Direction) {
    val v = field[p]
    val p1: Position
    val p2: Position
    when (v) {
        '[' -> {
            p1 = p
            p2 = p + RIGHT
        }

        ']' -> {
            p1 = p + LEFT
            p2 = p
        }

        else -> {
            throw IllegalStateException()
        }
    }

    val q1 = p1 + d
    when (field[q1]) {
        '[', ']' -> moveDoubleBoxVertically(field, q1, d)
        '.' -> {} // do nothing
        else -> throw IllegalStateException()
    }
    val q2 = p2 + d
    when (field[q2]) {
        '[', ']' -> moveDoubleBoxVertically(field, q2, d)
        '.' -> {} // do nothing
        else -> throw IllegalStateException()
    }

    field[q1] = '['
    field[q2] = ']'
    field[p1] = '.'
    field[p2] = '.'
}
