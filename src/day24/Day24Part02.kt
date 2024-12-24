package day24

import kotlin.random.Random

const val INPUT_SIZE = 45

fun main() {
  val (_, gates) = readInput()
  val validatedGates = mutableSetOf<String>()
  val swapList = mutableListOf<Pair<String, String>>()
  for (i in (1..INPUT_SIZE)) {
    while (true) {
      val (result, usedGates) = validateAddition(i, gates, swapList)
      if (result) {
        validatedGates += usedGates
        println("Validated $i bits")
        break
      }
      val candidates = usedGates - validatedGates
      println("Found error for $i bits. Candidate gates: $candidates")
      val fixedPairs = fixGates(i, candidates, validatedGates, swapList, gates)
      if (fixedPairs.isEmpty()) {
        println("Can't find fixed pairs for $candidates")
        return
      }
      println("Fixed pairs $fixedPairs")
      swapList += fixedPairs
    }
  }
}

fun fixGates(
    bits: Int,
    candidates: Set<String>,
    validatedGates: Set<String>,
    swapList: List<Pair<String, String>>,
    gates: Map<String, Gate>
): List<Pair<String, String>> {
  for (c in candidates) {
    for (d in gates.keys - validatedGates) {
      if (validateAddition(bits, gates, swapList + Pair(c, d)).first) {
        println(listOf(Pair(c, d)))
      }
    }
  }
  return listOf()
}

fun validateAddition(
    bits: Int,
    gates: Map<String, Gate>,
    swap: List<Pair<String, String>>,
): Pair<Boolean, Set<String>> {
  val allUsedGates = mutableSetOf<String>()
  for (i in (1..10000)) {
    val x = Random.nextLong(1L shl INPUT_SIZE)
    val y = Random.nextLong(1L shl INPUT_SIZE)
    val (z, usedGates) = eval(x, y, bits, gates, swap)
    val sum = (x + y).mod(1L shl bits)
    if (sum != z) {
      return Pair(false, usedGates)
    }
    allUsedGates += usedGates
  }
  return Pair(true, allUsedGates)
}

fun eval(
    x: Long,
    y: Long,
    bits: Int,
    gates: Map<String, Gate>,
    swap: List<Pair<String, String>>,
): Pair<Long, Set<String>> {
  val input =
      (0..<INPUT_SIZE)
          .flatMap { i ->
            listOf(
                String.format("x%02d", i) to ((x and (1L shl i)) != 0L),
                String.format("y%02d", i) to ((y and (1L shl i)) != 0L),
            )
          }
          .toMap()

  val cache: MutableMap<String, Boolean> = mutableMapOf()
  return (0..<bits)
      .map { i ->
        val (v, usedGates) = eval(String.format("z%02d", i), input, gates, swap, cache)
        Pair(if (v) 1L shl (i) else 0L, usedGates)
      }
      .reduce { acc, p -> Pair(acc.first + p.first, acc.second + p.second) }
}

fun eval(
    name: String,
    input: Map<String, Boolean>,
    gates: Map<String, Gate>,
    swap: List<Pair<String, String>>,
    cache: MutableMap<String, Boolean> = mutableMapOf(),
    usedGates: Set<String> = setOf()
): Pair<Boolean, Set<String>> {
  (input[name] ?: cache[name])?.let {
    return Pair(it, usedGates)
  }
  val swappedName =
      swap.find { it.first == name }?.second ?: swap.find { it.second == name }?.first ?: name
  if (usedGates.contains(swappedName)) {
    return Pair(false, usedGates)
  }
  val gate = gates[swappedName]!!
  val (v1, usedGates1) = eval(gate.i1, input, gates, swap, cache, usedGates + swappedName)
  val (v2, usedGates2) = eval(gate.i2, input, gates, swap, cache, usedGates + swappedName)
  val v = gate.gateType.eval(v1, v2)
  cache[swappedName] = v
  return Pair(v, usedGates1 + usedGates2)
}

fun generateNPairs(n: Int, to: Int): List<List<Pair<Int, Int>>> {
  val sequences = (0..<n).map { generatePairs(to) }
  return sequence {
        val iterators = sequences.map { it.iterator() }.toMutableList()
        val values = iterators.map { it.next() }.toMutableList()
        do {
          var progress = false
          for (i in iterators.indices.reversed()) {
            if (iterators[i].hasNext()) {
              values[i] = iterators[i].next()
              for (j in ((i + 1)..<(iterators.size - 1))) {
                iterators[j] = sequences[j].iterator()
              }
              yield(values.map { it })
              progress = true
              break
            }
          }
        } while (progress)
      }
      .filter {
        it.map { pair -> pair.first }.zipWithNext().all { pair -> pair.first < pair.second }
      }
      .toList()
      .distinct()
}

fun generatePairs(to: Int, from: Int = 0): Sequence<Pair<Int, Int>> {
  return generateSequence(Pair(from, from + 1)) { prev ->
    if (prev.second < to - 1) {
      Pair(prev.first, prev.second + 1)
    } else if (prev.first < to - 2) {
      Pair(prev.first + 1, prev.first + 2)
    } else {
      null
    }
  }
}
