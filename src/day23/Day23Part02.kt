package day23

fun main() {
  val nodes = readGraph()
  println(
      nodes.values
          .flatMap(::getCompleteGraphs)
          .distinct()
          .maxByOrNull { it.size }!!
          .map { it.name }
          .sorted()
          .joinToString(","))
}

fun getCompleteGraphs(node: Node): List<Set<Node>> {
  return node.edges
      .map { n ->
        val completeGraph = mutableSetOf(node, n)
        node.edges.map { m ->
          if (m.edges.containsAll(completeGraph)) {
            completeGraph += m
          }
        }
        completeGraph
      }
      .distinct()
}
