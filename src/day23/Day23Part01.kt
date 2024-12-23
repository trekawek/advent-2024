package day23

fun main() {
  val nodes = readGraph()
  println(
      nodes.values
          .flatMap { node -> findCycles(node, 3) }
          .map { it.toSet() }
          .distinct()
          .filter { it.any { n -> n.name.startsWith('t') } }
          .size)
}

fun readGraph(): Map<String, Node> {
  val nodes: MutableMap<String, Node> = mutableMapOf()
  for (pair in generateSequence(::readlnOrNull).map { it.split('-') }) {
    for (name in pair) {
      if (!nodes.contains(name)) {
        nodes[name] = Node(name)
      }
    }
    for (n1 in pair) {
      for (n2 in pair) {
        if (n1 == n2) {
          continue
        }
        nodes[n1]!!.edges += nodes[n2]!!
        nodes[n2]!!.edges += nodes[n1]!!
      }
    }
  }
  return nodes
}

fun findCycles(
    start: Node,
    cycleSize: Int,
    n: Node = start,
    visited: List<Node> = listOf()
): List<List<Node>> {
  if (visited.size == cycleSize && n == start) {
    return listOf(visited)
  }
  if (visited.size > cycleSize) {
    return listOf()
  }
  if (visited.contains(n)) {
    return listOf()
  }
  return n.edges.flatMap { m -> findCycles(start, cycleSize, m, visited + n) }
}

class Node(val name: String, val edges: MutableSet<Node> = mutableSetOf()) {
  override fun toString() = name
}
