package day01

import kotlin.math.absoluteValue
import kotlin.text.toInt

fun main() {
    println(
        generateSequence(::readlnOrNull).toList().map(::parseLine).unzip().run { first.sorted().zip(second.sorted()) }
            .sumOf { (it.first - it.second).absoluteValue })
}

internal fun parseLine(line: String) =
    line.split(" ").filter(String::isNotBlank).map(String::toInt).run { Pair(this[0], this[1]) }