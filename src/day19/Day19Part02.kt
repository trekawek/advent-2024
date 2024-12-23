package day19

fun main() {
  val patterns = readln().split(',').map(String::trim)
  val designs = generateSequence(::readlnOrNull).filter(String::isNotBlank).toList()
  println(
      designs.sumOf {
        val res = countPossibleArrangements(it, patterns)
        res
      })
}

fun countPossibleArrangements(
    design: String,
    patterns: List<String>,
    cache: MutableMap<String, Long> = mutableMapOf()
): Long {
  if (cache.contains(design)) {
    return cache[design]!!
  }
  if (design.isEmpty()) {
    return 1
  }
  val results =
      patterns
          .filter { design.startsWith(it) }
          .sumOf { countPossibleArrangements(design.removePrefix(it), patterns, cache) }
  cache[design] = results
  return results
}
