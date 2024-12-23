package day21

fun main() {
  println(generateSequence(::readlnOrNull).sumOf { getComplexity(it, 2) })
}

fun getComplexity(code: String, layers: Int): Long {
  val sequenceLen = getSequenceLength(code, layers)
  val numeric = code.toList().filter(Char::isDigit).joinToString("").toInt()
  println("$code: $sequenceLen * $numeric = ${sequenceLen * numeric}")
  return sequenceLen * numeric
}

fun getSequenceLength(code: String, layers: Int): Long {
  val sink = Sink()
  var delegate: ButtonRequester = sink
  for (i in (0..<layers)) {
    delegate = Keypad.newDirectionalKeypad(delegate)
  }

  val keypad = Keypad.newNumericKeypad(delegate)
  return keypad.request(code.toList()).first
}
