import java.io.File

fun main(args: Array<String>) {
    println("First test passes: ${
        getHighestOutputFromAllPhaseSequences(
                "3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0"
        ) == 43210
    }")
    println("Second test passes: ${
        getHighestOutputFromAllPhaseSequences(
                "3,23,3,24,1002,24,10,24,1002,23,-1,23,101,5,23,23,1,24,23,23,4,23,99,0,0"
        ) == 54321
    }")
    println("Third test passes: ${
        getHighestOutputFromAllPhaseSequences(
                "3,31,3,32,1002,32,10,32,1001,31,-2,31,1007,31,0,33,1002,33,7,33,1,33,31,31,1,32,31,31,4,31,99,0,0,0"
        ) == 65210
    }")
    println("From input we get: ${getHighestOutputFromAllPhaseSequences(File("input_day7").readText())}")
}

fun getHighestOutputFromAllPhaseSequences(tape: String): Int? {
    val program = tape.split(",").map { it.toInt() }
    val phaseSequencesWithOutputs = getEveryCombinationOfCharacters("01234").map {
        getOutputForPhaseSequence(it, program)
    }
    return phaseSequencesWithOutputs.max()
}

fun getEveryCombinationOfCharacters(input: String): List<String> {
    return comboHelper(listOf(Pair("", input)), emptyList())
}

tailrec fun comboHelper(queue: List<Pair<String, String>>, acc: List<String>): List<String> {
    if (queue.isEmpty()) return acc
    val pair = queue.first()
    if (pair.second.isEmpty()) return comboHelper(queue.drop(1), acc + listOf(pair.first))
    val newCandidates = pair.second.map {
        val splitIdx = pair.second.indexOf(it)
        Pair(pair.first + it, pair.second.substring(0, splitIdx) + pair.second.substring(splitIdx + 1))
    }
    return comboHelper(queue.drop(1) + newCandidates, acc)
}

fun getOutputForPhaseSequence(phaseSequence: String, program: List<Int>): Int {
    val phaseSettings = phaseSequence.map { Integer.parseInt(it.toString()) }
    var output = 0
    phaseSettings.forEach {
        output = recurseCompute3(program, 0, listOf(it, output), 0).second
    }
    return output
}

tailrec fun recurseCompute3(tape: List<Int>, offset: Int, input: List<Int>, output: Int): Pair<List<Int>, Int> {
    val opcode = tape[offset]
    if (opcode == 99) {
        return Pair(tape, output)
    }

    val result = when (opcode % 10) {
        1 -> operation(Op.ADD, opcode / 100, offset, tape)
        2 -> operation(Op.MUL, opcode / 100, offset, tape)
        3 -> input.first()
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
    }
    return recurseCompute3(tapeModified, when (opcode % 10) {
        1, 2, 7, 8 -> offset + 4
        3, 4 -> offset + 2
        5 -> branch(true, offset, opcode / 100, tape)
        6 -> branch(false, offset, opcode / 100, tape)
        else -> throw Exception("Unknown opcode: ${opcode % 10}")
    },
            if (opcode % 10 == 3) input.drop(1) else input,
            if (opcode % 10 == 4) result else output)
}