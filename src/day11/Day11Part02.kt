package day11

fun main() {
    println(readInput().sumOf { getStonesCount(it, 75) })
}

private data class Node(private val value: Long, val counts: MutableMap<Int, Long>)

private val nodesByValue = mutableMapOf<Long, Node>()
private fun getStonesCount(v: Long, iterations: Int): Long {
    if (iterations == 0) {
        return 1
    }
    val node = if (nodesByValue.contains(v)) {
        nodesByValue[v]!!
    } else {
        val newNode = Node(v, mutableMapOf())
        nodesByValue[v] = newNode
        newNode
    }
    val cachedCount = node.counts[iterations]
    if (cachedCount != null) {
        return cachedCount
    }
    val count = eval(v).sumOf { getStonesCount(it, iterations - 1) }
    node.counts[iterations] = count
    return count
}