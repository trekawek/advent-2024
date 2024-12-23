package day19

fun main() {
  val patterns = readln().split(',').map(String::trim)
  val designs = generateSequence(::readlnOrNull).filter(String::isNotBlank)
  println(designs.filter { isPossible(it, patterns) }.count())
}

fun isPossible(design: String, patterns: List<String>): Boolean {
  if (design.isEmpty()) {
    return true
  }
  for (p in patterns) {
    if (design.startsWith(p)) {
      if (isPossible(design.removePrefix(p), patterns)) {
        return true
      }
    }
  }
  return false
}
