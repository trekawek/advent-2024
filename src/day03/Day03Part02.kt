package day03

private val pattern = Regex("(?<mul>mul\\((?<arg1>\\d{1,3}),(?<arg2>\\d{1,3})\\))|(?<do>do\\(\\))|(?<dont>don't\\(\\))")

fun main() {
    println(
        generateSequence(::readlnOrNull).toList().flatMap(::parse)
            .fold(State()) { state, token -> token.apply(state) }.sum
    )
}

private fun parse(line: String) =
    pattern.findAll(line).map {
        when {
            it.groups["mul"] != null -> Token.Mul(it.groups["arg1"]!!.value.toInt(), it.groups["arg2"]!!.value.toInt())
            it.groups["do"] != null -> Token.Do
            it.groups["dont"] != null -> Token.Dont
            else -> null
        }
    }.filterNotNull()


data class State(val sum: Int = 0, val enabled: Boolean = true)

sealed interface Token {
    fun apply(state: State): State

    data class Mul(val i: Int, val j: Int) : Token {
        override fun apply(state: State) = if (state.enabled) {
            State(state.sum + i * j, enabled = true)
        } else {
            state
        }
    }

    data object Do : Token {
        override fun apply(state: State): State {
            return State(state.sum, enabled = true)
        }
    }

    data object Dont : Token {
        override fun apply(state: State): State {
            return State(state.sum, enabled = false)
        }
    }
}
