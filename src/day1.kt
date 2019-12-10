import java.io.File

fun main(args: Array<String>) {
    println("Part one: ${partOne()}")
    println("Part two: ${partTwo()}")
}

fun partOne(): Int {
    val input = File("input").useLines { it.toList() }
    return input.fold(0) {acc, str ->
        val mass = str.toInt()
        acc + (mass / 3) - 2
    }
}

fun partTwo(testAmt: String? = null): Int {
    val input = if (testAmt != null) listOf(testAmt) else File("input").useLines { it.toList() }
    return input.fold(0) {acc, str ->
        val mass = str.toInt()
        getFuelForMass(mass) + acc
    }
}

fun getFuelForMass(mass: Int): Int {
    return fuelForMassHelper(mass, 0)
}

tailrec fun fuelForMassHelper(mass: Int, acc: Int): Int {
    val fuelForMass = (mass / 3) - 2
    if (mass < 4 || fuelForMass < 0) return acc
    return fuelForMassHelper(fuelForMass, acc + fuelForMass)
}