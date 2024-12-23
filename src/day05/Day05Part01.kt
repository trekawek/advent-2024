package day05

fun main() {
  val order = buildOrder()
  println(readUpdates().filter { isInOrder(it, order) }.map { it[it.size / 2] }.sum())
}

internal fun buildOrder(): Map<Int, Set<Int>> {
  val orderBuilder = mutableMapOf<Int, MutableSet<Int>>()
  generateSequence(::readlnOrNull)
      .takeWhile { it.isNotBlank() }
      .map { it.split('|').run { Pair(this[0].toInt(), this[1].toInt()) } }
      .forEach { p ->
        val set = orderBuilder[p.first] ?: mutableSetOf()
        set.add(p.second)
        orderBuilder[p.first] = set
      }
  return orderBuilder.mapValues { e -> e.value.toSet() }.toMap()
}

internal fun readUpdates() =
    generateSequence(::readlnOrNull).map { it.split(',').map(String::toInt) }

internal fun isInOrder(list: List<Int>, order: Map<Int, Set<Int>>) =
    list.zipWithNext().none { order.compare(it.first, it.second) == 1 }

internal fun Map<Int, Set<Int>>.compare(i: Int, j: Int) =
    if (this[i]?.contains(j) == true) {
      -1
    } else if (this[j]?.contains(i) == true) {
      1
    } else if (i == j) {
      0
    } else {
      throw IllegalArgumentException("Can't compare $i and $j")
    }
