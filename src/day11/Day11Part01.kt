package day11

import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

fun main() {
  println(readInput().repeatFlatMap(25, ::eval).count())
}

fun readInput() = readln().split(' ').map(String::toLong)

private fun <T> List<T>.repeatFlatMap(iterations: Int, transform: ((T) -> List<T>)): List<T> {
  var result = this
  repeat(iterations) { i ->
    result = result.flatMap(transform)
    println("# Iteration: ${i + 1}\tSize: ${result.size}")
  }
  return result
}

fun eval(l: Long): List<Long> {
  val result =
      if (l == 0L) {
        listOf(1L)
      } else {
        val digits = (floor(log10(l.toDouble())) + 1)
        if (digits.toInt() % 2 == 0) {
          val pow10 = 10.toDouble().pow((digits / 2)).toLong()
          val firstHalf = l / pow10
          val secondHalf = l % pow10
          listOf(firstHalf, secondHalf)
        } else {
          listOf(l * 2024)
        }
      }
  return result
}
