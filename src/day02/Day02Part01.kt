package day02

fun main() {
  println(
      generateSequence(::readlnOrNull)
          .toList()
          .map(::parseLine)
          .map {
            if (it.last() < it.first()) {
              it.reversed()
            } else {
              it
            }
          }
          .count(::isSafe))
}

internal fun parseLine(line: String): List<Int> = line.split(' ').map { it.toInt() }

internal fun isSafe(line: List<Int>) =
    line.zipWithNext().map { it.second - it.first }.all { it in 1..3 }
