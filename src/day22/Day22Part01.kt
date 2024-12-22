package day22

fun main() {
    println(generateSequence(::readlnOrNull).map(String::toLong).map { hashRounds(it, 2000) }.sum())
}


fun hashRounds(i: Long, rounds: Int): Long {
    var result = i
    for (i in (0..<rounds)) {
        result = hash(result)
    }
    return result
}

fun hash(i: Long): Long {
    var result = i
    result = prune(mix(result, result * 64))
    result = prune(mix(result, result / 32))
    result = prune(mix(result, result * 2048))
    return result
}

fun mix(i: Long, j: Long): Long {
    return i xor j
}

fun prune(i: Long): Long {
    return i.mod(16777216).toLong()
}