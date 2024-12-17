package day17

import java.math.BigInteger

fun main() {
    println(findCode(listOf(2, 4, 1, 2, 7, 5, 4, 5, 1, 3, 5, 5, 0, 3, 3, 0)))
}

val digitsToCodes = (0..<1024).map { i -> Pair(i, program(i).first()) }.groupBy { it.second }
    .mapValues { e -> e.value.map { it.first }.sorted() }

private fun findCode(digits: List<Int>, i: Int = 0, partialCode: BigInteger = BigInteger.ZERO): List<BigInteger> {
    if (partialCode < BigInteger.ZERO) {
        throw IllegalArgumentException("Invalid partial code at i=$i: $partialCode")
    }
    if (i >= digits.size) {
        return listOf(partialCode)
    }

    return digitsToCodes[digits[i]]!!.asSequence().flatMap { c ->
        if (i == 0 || fitsIntoPartial(partialCode, c, i)) {
            findCode(digits, i + 1, mergeIntoPartial(partialCode, c, i))
        } else {
            listOf()
        }
    }.toSet().filter { program(it) == digits }.sorted()
}

// partialCode:    SSSS SSS UUU
// candidate:       TTT TTT
//                === ===
private fun fitsIntoPartial(partialCode: BigInteger, candidate: Int, i: Int): Boolean {
    return (partialCode shr (i * 3)).and(0b111_111) == (candidate and 0b111_111).toBigInteger()
}

// partialCode:  T SSS SSS UUU
// candidate: TTTT SSS SSS
//            TTT SSS SSS UUU
private fun mergeIntoPartial(partialCode: BigInteger, candidate: Int, i: Int): BigInteger {
    return partialCode.or((candidate.toBigInteger().shl(i * 3)))
}

private fun program(start: Int) = program(BigInteger.valueOf(start.toLong()))

/**
 * 0000 bst(A)     B = A & 0b111
 * 0002 bxl(2)     B = B ^ 0b010
 * 0004 cdv(B)     C = A >> B
 * 0006 bxc()      B = B ^ C
 * 0008 bxl(3)     B = B ^ 0b011
 * 0010 out(B)     out B
 * 0012 adv(3)     A = A >> 3
 * 0014 jnz(0)     if (A != 0) goto 0
 */
private fun program(start: BigInteger): List<Int> {
    var a = start
    val result = mutableListOf<Int>()
    do {
        val b = a.and(0b111).xor(2)
        val o = (a.shr(b)).and(0b111).xor(b).xor(3)
        a = a shr 3
        result += o.toInt()
    } while (a != BigInteger.ZERO)
    return result
}

fun BigInteger.and(v: Int) = this.and(BigInteger.valueOf(v.toLong()))
fun BigInteger.xor(v: Int) = this.xor(BigInteger.valueOf(v.toLong()))
fun BigInteger.shr(v: BigInteger) = this.shr(v.toInt())
