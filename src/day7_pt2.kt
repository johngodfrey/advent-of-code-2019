import java.io.File

fun main(args: Array<String>) {
    println("First test passes: ${
    getOutputFromFeedbackLoop(
        "3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5"
    ) == 139629729
    }")
    println("Second test passes: ${
    getOutputFromFeedbackLoop(
        "3,52,1001,52,-5,52,3,53,1,52,56,54,1007,54,5,55,1005,55,26,1001,54,-5,54,1105,1,12,1,53,54,53,1008,54,0,55,1001,55,1,55,2,53,55,53,4,53,1001,56,-1,56,1005,56,6,99,0,0,0,0,10"
    ) == 18216
    }")
    println("From input we get: ${getOutputFromFeedbackLoop(File("input_day7").readText())}")
}

fun getOutputFromFeedbackLoop(tape: String): Int? {
    val program = tape.split(",").map { it.toInt() }

    return getEveryCombinationOfCharacters("56789")
        .map { str ->
            str.map {
                getContextForPhaseSetting(it, program)
            }
        }
        .map { processContexts(it) }
        .max()
}

data class Context(val tape: List<Int>, val offset: Int, val io: Int, val lastOpcode: Int = 0)

fun processContexts(amplifiers: List<Context>): Int {
    var rotatingList = amplifiers
    while (!rotatingList.all { it.lastOpcode == 99 }) {
        rotatingList = rotatingList.drop(1) + listOf(computeForSingleInput(rotatingList.first().copy(io = rotatingList.last().io)))
    }
    return rotatingList.last().io
}

fun getContextForPhaseSetting(setting: Char, tape: List<Int>): Context {
    val phaseSetting = Integer.parseInt(setting.toString())
    return computeForSingleInput(Context(tape, 0, phaseSetting))
}

fun computeForSingleInput(input: Context): Context  {
    var opcode = input.tape[input.offset]
    var pointerPosition = input.offset
    var inputConsumed = false
    val tapeModified = input.tape.toMutableList()
    var output = 0

    fun consumeInput(result: Int) {
        tapeModified[tapeModified[pointerPosition + 1]] = result
        inputConsumed = true
    }

    while (!(inputConsumed && opcode == 3) && opcode != 99) {
        val result = when (opcode % 10) {
            1 -> operation(Op.ADD, opcode / 100, pointerPosition, tapeModified)
            2 -> operation(Op.MUL, opcode / 100, pointerPosition, tapeModified)
            3 -> input.io
            4 -> if (opcode / 100 == 1) tapeModified[pointerPosition + 1] else tapeModified[tapeModified[pointerPosition + 1]]
            5, 6 -> 0
            7 -> comparison(Sign.LESS_THAN, opcode / 100, pointerPosition, tapeModified)
            8 -> comparison(Sign.EQUALS, opcode / 100, pointerPosition, tapeModified)
            else -> throw Exception("Unknown opcode: ${opcode % 10}")
        }

        when (opcode % 10) {
            1, 2, 7, 8 -> tapeModified[tapeModified[pointerPosition + 3]] = result
            3 -> consumeInput(result)
            4 -> output = result
        }
        pointerPosition = when (opcode % 10) {
            1, 2, 7, 8 -> pointerPosition + 4
            3, 4 -> pointerPosition + 2
            5 -> branch(true, pointerPosition, opcode / 100, tapeModified)
            6 -> branch(false, pointerPosition, opcode / 100, tapeModified)
            else -> throw Exception("Unknown opcode: ${opcode % 10}")
        }

        opcode = tapeModified[pointerPosition]
    }
    return Context(tapeModified, pointerPosition, output, opcode)
}
