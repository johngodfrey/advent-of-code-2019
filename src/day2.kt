import java.io.File

fun main() {
    val input = File("input_day2").readText()
    println("Using puzzle input gives ${compute(input)}")
    println("part2Compute(): ${part2Compute(input)}")
}

fun compute(input: String): List<String> {
    val opcodesWithOperands = listOf(1, 12, 2) + input.split(",").map { it.toInt() }.drop(3)
    return recurseCompute(opcodesWithOperands, 0).map { it.toString() }
}

fun part2Compute(input: String): Pair<Int, Int> {
    for (noun in 0..99) {
        for (verb in 0..99) {
            if (recurseCompute(
                    listOf(1, noun, verb) + input.split(",")
                        .map { it.toInt() }.drop(3), 0)[0] == 19690720) {
                return Pair(noun, verb)
            }
        }
    }
    return Pair(-1, -1)
}

tailrec fun recurseCompute(input: List<Int>, iteration: Int): List<Int> {
    val offset = iteration * 4
    val opcode = input[offset]
    if (opcode == 99) {
        return input
    }
    val result = when (opcode) {
        1 -> input[input[offset + 1]] + input[input[offset + 2]]
        2 -> input[input[offset + 1]] * input[input[offset + 2]]
        else -> throw Exception("Unknown opcode")
    }
    val inputModified = input.toMutableList()
    inputModified[input[offset + 3]] = result
    return recurseCompute(inputModified, iteration + 1)
}