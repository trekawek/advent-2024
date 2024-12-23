package day07

fun main() {
  println(
      parseInput()
          .filter { (lhs, rhs) -> checkIfSolutionExist(lhs, rhs) }
          .map { (lhs, _) -> lhs }
          .sum())
}

internal fun parseInput() =
    generateSequence(::readlnOrNull).map {
      it.split(":").run {
        val lhs = this[0].trim().toLong()
        val rhs =
            this[1].split(' ').map(String::trim).filterNot(String::isEmpty).map(String::toLong)
        Pair(lhs, rhs)
      }
    }

private fun checkIfSolutionExist(result: Long, args: List<Long>): Boolean {
  val operatorCount = args.size - 1
  for (operatorBitmask in (0..<(1 shl operatorCount))) {
    var r = args.first()
    for (i in (1..<args.size)) {
      if (operatorBitmask and (1 shl i - 1) == 0) {
        r += args[i]
      } else {
        r *= args[i]
      }
      if (r > result) {
        break
      }
    }
    if (r == result) {
      return true
    }
  }
  return false
}
