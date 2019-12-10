import java.io.File

fun main(args: Array<String>) {
    val input = File("input_day5").readText()
    compute2(input)
}

fun compute2(input: String): List<Int> = recurseCompute2(input.split(",").map { it.toInt() }, 0)

enum class Op {
    ADD, MUL
}

tailrec fun recurseCompute2(input: List<Int>, offset: Int): List<Int> {
    val opcode = input[offset]
    // println("opcode: $opcode, offset: $offset")
    if (opcode == 99) {
        return input
    }

    val result = when (opcode.rem(10)) {
        1 -> operation(Op.ADD, opcode / 100, offset, input)
        2 -> operation(Op.MUL, opcode / 100, offset, input)
        3 -> 1
        4 -> if (opcode / 100 == 1) input[offset + 1] else input[input[offset + 1]]
        else -> throw Exception("Unknown opcode: ${opcode.rem( 10)}")
    }

    val inputModified = input.toMutableList()
    when (opcode.rem(10)) {
        1, 2 -> inputModified[input[offset + 3]] = result
        3 -> inputModified[input[offset + 1]] = result
        4 -> println(result)
    }
    return recurseCompute2(inputModified, when (opcode.rem(10)) {
        1, 2 -> offset + 4
        3, 4 -> offset + 2
        else -> throw Exception("Unknown opcode: ${opcode.rem(10)}")
    })
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