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
    // println("opcode: $opcode, offset: $offset")
    if (opcode == 99) {
        return tape
    }

    val result = when (opcode.rem(10)) {
        1 -> operation(Op.ADD, opcode / 100, offset, tape)
        2 -> operation(Op.MUL, opcode / 100, offset, tape)
        3 -> input
        4 -> if (opcode / 100 == 1) tape[offset + 1] else tape[tape[offset + 1]]
        5, 6 -> 0
        7 -> comparison(Sign.LESS_THAN, opcode / 100, offset, tape)
        8 -> comparison(Sign.EQUALS, opcode / 100, offset, tape)
        else -> throw Exception("Unknown opcode: ${opcode.rem( 10)}")
    }

    val tapeModified = tape.toMutableList()
    when (opcode.rem(10)) {
        1, 2, 7, 8 -> tapeModified[tape[offset + 3]] = result
        3 -> tapeModified[tape[offset + 1]] = result
        4 -> println(result)
    }
    return recurseCompute2(tapeModified, when (opcode.rem(10)) {
        1, 2, 7, 8 -> offset + 4
        3, 4 -> offset + 2
        5 -> branch(5, offset, opcode / 100, tape)
        6 -> branch(6, offset, opcode / 100, tape)
        else -> throw Exception("Unknown opcode: ${opcode.rem(10)}")
    }, input)
}

fun operation(type: Op, mode: Int, offset: Int, input: List<Int>): Int {
    val operand1 = if (mode.rem(10) == 1) input[offset + 1] else input[input[offset + 1]]
    val operand2 = if (mode / 10 == 1) input[offset + 2] else input[input[offset + 2]]
    // println("About to return, $operand1 ${if (type == Op.ADD) "+" else "*"} $operand2")
    return when (type) {
        Op.ADD -> operand1 + operand2
        Op.MUL -> operand1 * operand2
    }
}

enum class Sign {
    LESS_THAN, EQUALS
}

fun comparison(type: Sign, mode: Int, offset: Int, input: List<Int>): Int {
    // compute whether params are set to return 1 or just . . .
    return 0
}

fun branch(opcode: Int, offset: Int, mode: Int, input: List<Int>): Int {
    // compute whether to jump to a new position or just . . .
    return offset + 2
}