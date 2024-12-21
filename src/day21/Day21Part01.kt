package day21

fun main() {
    println(generateSequence(::readlnOrNull).sumOf { getComplexity(it, 2) })
}

fun getComplexity(code: String, layers: Int): Int {
    val sequenceLen = getSequence(code, layers).size
    val numeric = code.toList().filter(Char::isDigit).joinToString("").toInt()
    println("$code: $sequenceLen * $numeric = ${sequenceLen * numeric}")
    return sequenceLen * numeric
}

fun getSequence(code: String, layers: Int): List<Char> {
    val sink = Sink()
    var delegate: ButtonRequester = sink
    for (i in (0..<layers)) {
        delegate = Keypad.newDirectionalKeypad(delegate)
    }

    val keypad = Keypad.newNumericKeypad(delegate)
    return keypad.request(code.toList()).first
}