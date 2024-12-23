package day07

fun main() {
  println(
      parseInput()
          .filter { (lhs, rhs) -> checkIfSolutionExist(lhs, rhs) }
          .map { (lhs, _) -> lhs }
          .sum())
}

private fun checkIfSolutionExist(result: Long, args: List<Long>): Boolean {
  val operatorList = OperatorList(args.size - 1)
  do {
    var r = args.first()
    for (i in (1..<args.size)) {
      r =
          when (operatorList[i - 1]) {
            Operator.PLUS -> r + args[i]
            Operator.MULTI -> r * args[i]
            Operator.CAT -> "$r${args[i]}".toLong()
          }
      if (r > result) {
        break
      }
    }
    if (r == result) {
      return true
    }
  } while (operatorList.next())
  return false
}

private enum class Operator {
  PLUS,
  MULTI,
  CAT
}

private class OperatorList(length: Int) {
  private val operators = MutableList(length) { Operator.PLUS }

  operator fun get(i: Int): Operator {
    return operators[i]
  }

  fun next(): Boolean {
    for (i in operators.indices) {
      val e = (operators[i].ordinal + 1) % 3
      operators[i] = Operator.entries[e]
      if (e != 0) {
        return true
      }
    }
    return false
  }
}
