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
          .count(::isSafeWithElementRemoved))
}

private fun isSafeWithElementRemoved(line: List<Int>) =
    if (isSafe(line)) {
      true
    } else {
      line.indices
          .map { i -> line.subList(0, i) + line.subList(i + 1, line.size) }
          .any { isSafe(it) }
    }
