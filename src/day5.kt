import java.io.File

fun main(args: Array<String>) {
    val input = File("input_day5").readText()
    compute2(input, 1)
    compute2(input, 5)
}

fun compute2(tape: String, input: Int): List<Int> = recurseCompute2(tape.split(",").map { it.toInt() }, 0, input)

enum class Op {
    ADD, MUL
}

tailrec fun recurseCompute2(tape: List<Int>, offset: Int, input: Int): List<Int> {
    val opcode = tape[offset]
    if (opcode == 99) {
        return tape
    }

    val result = when (opcode % 10) {
        1 -> operation(Op.ADD, opcode / 100, offset, tape)
        2 -> operation(Op.MUL, opcode / 100, offset, tape)
        3 -> input
        4 -> if (opcode / 100 == 1) tape[offset + 1] else tape[tape[offset + 1]]
        5, 6 -> 0
        7 -> comparison(Sign.LESS_THAN, opcode / 100, offset, tape)
        8 -> comparison(Sign.EQUALS, opcode / 100, offset, tape)
        else -> throw Exception("Unknown opcode: ${opcode % 10}")
    }

    val tapeModified = tape.toMutableList()
    when (opcode % 10) {
        1, 2, 7, 8 -> tapeModified[tape[offset + 3]] = result
        3 -> tapeModified[tape[offset + 1]] = result
        4 -> println(result)
    }
    return recurseCompute2(tapeModified, when (opcode % 10) {
        1, 2, 7, 8 -> offset + 4
        3, 4 -> offset + 2
        5 -> branch(true, offset, opcode / 100, tape)
        6 -> branch(false, offset, opcode / 100, tape)
        else -> throw Exception("Unknown opcode: ${opcode % 10}")
    }, input)
}

fun operation(type: Op, mode: Int, offset: Int, input: List<Int>): Int {
    val operand1 = getPositionalOrImmediateOperand(mode % 10, input, offset + 1)
    val operand2 = getPositionalOrImmediateOperand(mode / 10, input, offset + 2)
    return when (type) {
        Op.ADD -> operand1 + operand2
        Op.MUL -> operand1 * operand2
    }
}

enum class Sign {
    LESS_THAN, EQUALS
}

fun comparison(type: Sign, mode: Int, offset: Int, input: List<Int>): Int {
    val operand1 = getPositionalOrImmediateOperand(mode % 10, input, offset + 1)
    val operand2 = getPositionalOrImmediateOperand(mode / 10, input, offset + 2)
    return when (type) {
        Sign.LESS_THAN -> if (operand1 < operand2) 1 else 0
        Sign.EQUALS -> if (operand1 == operand2)1 else 0
    }
}

fun getPositionalOrImmediateOperand(modeDigit: Int, input: List<Int>, offset: Int): Int {
    return if (modeDigit == 1) input[offset] else input[input[offset]]
}

fun branch(predicate: Boolean, offset: Int, mode: Int, input: List<Int>): Int {
    val operand1 = getPositionalOrImmediateOperand(mode % 10, input, offset + 1)
    val operand2 = getPositionalOrImmediateOperand(mode / 10, input, offset + 2)
    if ((operand1 != 0 && predicate) || (operand1 == 0 && !predicate)) return operand2
    return offset + 3
}