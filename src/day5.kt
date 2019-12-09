tailrec fun recurseCompute2(input: List<Int>, iteration: Int): List<Int> { // change iteration to offset
    val offset = iteration * 4 // change iteration, just use offset
    val opcode = input[offset]
    if (opcode == 99) {
        return input
    }
    val result = when (opcode) {
        1 -> input[input[offset + 1]] + input[input[offset + 2]]
        2 -> input[input[offset + 1]] * input[input[offset + 2]]
        else -> throw Exception("Unknown opcode: $opcode")
    }
    val inputModified = input.toMutableList()
    inputModified[input[offset + 3]] = result // needs to account for opcodes that don't write here
    return recurseCompute2(inputModified, iteration + 1) //change iteration to offset
}