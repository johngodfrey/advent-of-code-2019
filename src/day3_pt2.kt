import java.io.File
import kotlin.math.absoluteValue

fun main() {
    println("First test passes: ${distanceToClosestIntersectionWithStepCount(
        Pair(
            "R8,U5,L5,D3",
            "U7,R6,D4,L4")
    ) == 30}")
    println("Second test passes: ${distanceToClosestIntersectionWithStepCount(
        Pair(
            "R75,D30,R83,U83,L12,D49,R71,U7,L72", 
            "U62,R66,U55,R34,D71,R55,D58,R83")
    ) == 610}")
    println("Third test passes: ${distanceToClosestIntersectionWithStepCount(
        Pair(
            "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51",
            "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7")
    ) == 410}")
    // val input = File("input_day3").useLines { it.toList() }
    // println("From input we get: ${distanceToClosestIntersectionWithStepCount(Pair(input[0], input[1]))}")
}

fun distanceToClosestIntersectionWithStepCount(input: Pair<String, String>): Int {
    val firstWirePath = getPathWithStepCount(input.first)
    val secondWirePath = getPathWithStepCount(input.second)

    return firstWirePath
        .drop(1)
        .toSet().intersect(secondWirePath.toSet())
        .map { it.first.absoluteValue + it.second.absoluteValue }
        .reduce { acc, i -> if (i < acc) i else acc }
}

fun getPathWithStepCount(input: String): List<Triple<Int, Int, Int>> {
    val path = mutableListOf(Triple(0, 0, 0))
    return input.split(",").flatMapTo(path, {
        plotPathWithStepCount(
            path.last(),
            it.drop(1).toInt(),
            Direction.valueOf(it[0].toString())
        )
    })
}

fun plotPathWithStepCount(startLocation: Triple<Int, Int, Int>, distance: Int, direction: Direction): List<Triple<Int, Int, Int>> {
    var counterX = startLocation.first
    var counterY = startLocation.second
    return when (direction) {
        Direction.R -> List(distance) { Triple(++counterX, counterY, startLocation.third + distance) }
        Direction.L -> List(distance) { Triple(--counterX, counterY, startLocation.third + distance) }
        Direction.U -> List(distance) { Triple(counterX, ++counterY, startLocation.third + distance) }
        Direction.D -> List(distance) { Triple(counterX, --counterY, startLocation.third + distance) }
    }
}