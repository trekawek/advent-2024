package day17

fun main() {
    println(Machine.readFromStdin().run().joinToString(","))
}

class Machine(var a: Long, private var b: Long, private var c: Long, val program: List<Int>) {
    private var pc: Int = 0

    fun updateA(a: Long): Machine {
        return Machine(a, b, c, program)
    }

    fun run(): List<Long> {
        println(this)
        val result = mutableListOf<Long>()
        while (pc < program.size) {
            if (pc == 0) {
                println("\na=${a.toString(2)}\nb=${b.toString(2)}")
            }
            val o = step()
            if (o != null) {
                result += o
                println("o=${o.toString(2)}")
            }
        }
        return result
    }

    fun asSequence() = generateSequence {
        if (pc < program.size) {
            step() ?: Long.MAX_VALUE
        } else {
            null
        }
    }.filter { it != Long.MAX_VALUE }

    fun step(): Long? {
        val opcode = Opcode.entries[program[pc]]
        val operand = program[pc + 1]
        val combo = getComboOperand(operand)
        //println("[$pc] $opcode($operand)")

        pc += 2
        when (opcode) {
            Opcode.adv -> a = a / (1 shl combo.toInt())
            Opcode.bxl -> b = b xor operand.toLong()
            Opcode.bst -> b = combo.mod(8).toLong()
            Opcode.jnz -> if (a != 0L) pc = operand
            Opcode.bxc -> b = b xor c
            Opcode.out_ -> return combo.mod(8).toLong()
            Opcode.bdv -> b = a / (1 shl combo.toInt())
            Opcode.cdv -> c = a / (1 shl combo.toInt())
        }
        return null
    }

    fun getComboOperand(o: Int): Long {
        return when (o) {
            in (0..3) -> o.toLong()
            4 -> a
            5 -> b
            6 -> c
            else -> throw IllegalArgumentException("Invalid combo operand $o")
        }
    }

    override fun toString(): String {
        return StringBuilder().run {
            append("A=$a, B=$b, C=$c, PC=$pc\n\n")
            for (i in program.indices step 2) {
                val opcode = Opcode.entries[program[i]]
                val operand = program[i + 1]
                append(String.format("%04d", i))
                append(" ")
                append("${opcode.toString().substring(0, 3)}(")
                append(
                    when (opcode.operandType) {
                        OperandType.literal -> operand
                        OperandType.combo -> {
                            when (operand) {
                                in (0..3) -> operand
                                4 -> "A"
                                5 -> "B"
                                6 -> "C"
                                else -> throw IllegalArgumentException("Invalid combo operand $operand")
                            }
                        }

                        OperandType.none -> ""
                    }
                )
                append(")\n")
            }
            append("\n")
        }.toString()
    }

    companion object {
        fun readFromStdin(): Machine {
            var a: Long = 0
            var b: Long = 0
            var c: Long = 0
            var program: List<Int> = listOf()
            for (line in generateSequence(::readlnOrNull).filter(String::isNotBlank)) {
                Regex("Register ([A-C]): (\\d+)").matchEntire(line)?.let {
                    when (it.groupValues[1]) {
                        "A" -> a = it.groupValues[2].toLong()
                        "B" -> b = it.groupValues[2].toLong()
                        "C" -> c = it.groupValues[2].toLong()
                    }
                }
                Regex("Program: ([\\d,]+)").matchEntire(line)?.let {
                    program = it.groupValues[1].split(",").map(String::toInt)
                }
            }
            return Machine(a, b, c, program)
        }
    }
}

enum class Opcode(val operandType: OperandType) {
    adv(OperandType.combo), bxl(OperandType.literal), bst(OperandType.combo), jnz(OperandType.literal), bxc(OperandType.none), out_(
        OperandType.combo
    ),
    bdv(OperandType.combo), cdv(OperandType.combo)
}

enum class OperandType {
    literal, combo, none
}