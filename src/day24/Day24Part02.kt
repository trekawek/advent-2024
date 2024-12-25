package day24

const val INPUT_SIZE = 45

// qff <-> qnw

val swap = listOf("qff" to "qnw")

fun main() {
  generateSequence(::readlnOrNull).takeWhile(String::isNotEmpty).toList()
  val gates =
      generateSequence(::readlnOrNull)
          .map {
            val match = Regex("([a-z0-9]+) ([A-Z]+) ([a-z0-9]+) -> ([a-z0-9]+)").matchEntire(it)!!
            val groupValues = match.groupValues
            GateNode(
                groupValues[4],
                groupValues[2],
                InputNode(groupValues[1]),
                InputNode(groupValues[3]))
          }
          .associateBy { it.name!! }
          .also { gates ->
            for (n in gates.values) {
              n.input1 = gates[n.input1.name()] ?: n.input1
              n.input2 = gates[n.input2.name()] ?: n.input2
            }
          }

  val swapped = mutableListOf<String>()
  for (i in (0..INPUT_SIZE)) {
    val broken = gates[String.format("z%02d", i)]!!
    val blueprint =
        if (i == INPUT_SIZE) {
          carryNode(i - 1)
        } else {
          mainNode(i)
        }

    if (broken.equiv(blueprint)) {
      println("Gates are OK for $i")
      continue
    }
    swapped += fix(gates, broken, blueprint)
  }
  println(swapped.sorted().joinToString(","))
}

private fun fix(gates: Map<String, GateNode>, broken: Node, blueprint: Node): List<String> {
  if (broken !is GateNode) {
    throw IllegalArgumentException("Broken node ${broken.name()} is not GateNode")
  }
  if (blueprint !is GateNode) {
    throw IllegalArgumentException("Blueprint node ${blueprint.name()} is not GateNode")
  }
  if (blueprint.equiv(broken)) {
    return listOf()
  }
  for (n in gates.values) {
    if (blueprint.equiv(n)) {
      swapNodes(gates, broken.name!!, n.name!!)
      return listOf(broken.name!!, n.name!!)
    }
  }
  return if (broken.input1.equiv(blueprint.input1)) {
    fix(gates, broken.input2, blueprint.input2)
  } else if (broken.input1.equiv(blueprint.input2)) {
    fix(gates, broken.input2, blueprint.input1)
  } else if (broken.input2.equiv(blueprint.input1)) {
    fix(gates, broken.input1, blueprint.input2)
  } else if (broken.input2.equiv(blueprint.input2)) {
    fix(gates, broken.input1, blueprint.input1)
  } else {
    throw IllegalArgumentException("Can't fix node ${broken.name}")
  }
}

private fun swapNodes(gates: Map<String, GateNode>, name1: String, name2: String) {
  println("Swapping nodes $name1 <-> $name2")
  val gate1 = gates[name1]!!
  val gate2 = gates[name2]!!

  gate1.name = name2
  gate2.name = name1

  for (n in gates.values) {
    if (n.input1 === gate1) {
      n.input1 = gate2
    } else if (n.input1 == gate2) {
      n.input1 = gate1
    }
    if (n.input2 === gate1) {
      n.input2 = gate2
    } else if (n.input2 == gate2) {
      n.input2 = gate1
    }
  }
}

private fun mainNode(i: Int): GateNode {
  val input1 = InputNode(String.format("x%02d", i))
  val input2 = InputNode(String.format("y%02d", i))
  val inputXor = GateNode(null, "XOR", input1, input2)
  return if (i > 0) {
    GateNode(null, "XOR", inputXor, carryNode(i - 1))
  } else {
    inputXor
  }
}

private fun carryNode(i: Int): GateNode {
  val input1 = InputNode(String.format("x%02d", i))
  val input2 = InputNode(String.format("y%02d", i))
  val inputAnd = GateNode(null, "AND", input1, input2)
  return if (i > 0) {
    val mainNode = mainNode(i)
    GateNode(null, "OR", GateNode(null, "AND", mainNode.input1, mainNode.input2), inputAnd)
  } else {
    inputAnd
  }
}

private data class GateNode(
    var name: String?,
    val op: String,
    var input1: Node,
    var input2: Node,
) : Node {
  override fun name() = name

  override fun printTree(indent: Int): String {
    return StringBuilder()
        .apply {
          append(" ".repeat(indent))
          if (name == null) {
            append("---")
          } else {
            append(name)
          }
          append(" $op\n")
          append(input1.printTree(indent + 2))
          append(input2.printTree(indent + 2))
        }
        .toString()
  }

  override fun equiv(n: Node): Boolean {
    if (n !is GateNode) {
      return false
    }
    if (n.op != op) {
      return false
    }
    return (n.input1.equiv(input1) && n.input2.equiv(input2)) ||
        (n.input1.equiv(input2) && n.input2.equiv(input1))
  }
}

private data class InputNode(val name: String) : Node {
  override fun name() = name

  override fun printTree(indent: Int): String {
    return StringBuilder()
        .apply {
          append(" ".repeat(indent))
          append(name)
          append("\n")
        }
        .toString()
  }

  override fun equiv(n: Node): Boolean {
    return this == n
  }
}

private interface Node {
  fun name(): String?

  fun printTree(indent: Int = 0): String

  fun equiv(n: Node): Boolean
}
