package day05

fun main() {
  val order = buildOrder()
  println(
      readUpdates()
          .filter { !isInOrder(it, order) }
          .map { it.sortedWith(order::compare) }
          .map { it[it.size / 2] }
          .sum())
}
