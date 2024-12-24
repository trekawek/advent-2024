package day24

fun main() {
  val (input, gates) = readInput()
  val eval = getEvalFunction(input, gates)
  println(
      gates.keys
          .filter { it.startsWith("z") }
          .sortedBy { it }
          .reversed()
          .map { if (eval(it)) 1 else 0 }
          .joinToString("")
          .toLong(2))
}

fun readInput(): Pair<Map<String, Boolean>, Map<String, Gate>> {
  val input =
      generateSequence(::readlnOrNull)
          .takeWhile(String::isNotEmpty)
          .map { it.split(':').map(String::trim).run { Pair(this[0], this[1].toInt() == 1) } }
          .toMap()
  val gates =
      generateSequence(::readlnOrNull)
          .map {
            val match = Regex("([a-z0-9]+) ([A-Z]+) ([a-z0-9]+) -> ([a-z0-9]+)").matchEntire(it)!!
            val groupValues = match.groupValues
            Gate(groupValues[1], groupValues[3], GateType.valueOf(groupValues[2]), groupValues[4])
          }
          .associateBy { it.o }
  return Pair(input, gates)
}

fun getEvalFunction(input: Map<String, Boolean>, gates: Map<String, Gate>): (String) -> Boolean {
  val cache: MutableMap<String, Boolean> = mutableMapOf()
  fun getValue(o: String): Boolean {
    return cache[o]
        ?: input[o]
        ?: gates[o]!!
            .let { gate -> gate.gateType.eval(getValue(gate.i1), getValue(gate.i2)) }
            .also { cache[o] = it }
  }
  return ::getValue
}

data class Gate(val i1: String, val i2: String, val gateType: GateType, val o: String)

enum class GateType {
  AND {
    override fun eval(i1: Boolean, i2: Boolean): Boolean {
      return i1 && i2
    }
  },
  OR {
    override fun eval(i1: Boolean, i2: Boolean): Boolean {
      return i1 || i2
    }
  },
  XOR {
    override fun eval(i1: Boolean, i2: Boolean): Boolean {
      return i1 xor i2
    }
  };

  abstract fun eval(i1: Boolean, i2: Boolean): Boolean
}
