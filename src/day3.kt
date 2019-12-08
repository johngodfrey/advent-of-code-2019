import java.io.File
import kotlin.math.absoluteValue

fun main(args: Array<String>) {
    println("First test passes: ${distanceToClosestIntersection(
        Pair(
            "R8,U5,L5,D3",
            "U7,R6,D4,L4")
    ) == 6}")
    println("Second test passes: ${distanceToClosestIntersection(
        Pair(
            "R75,D30,R83,U83,L12,D49,R71,U7,L72", 
            "U62,R66,U55,R34,D71,R55,D58,R83")
    ) == 159}")
    println("Third test passes: ${distanceToClosestIntersection(
        Pair(
            "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51",
            "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7")
    ) == 135}")
    val input = File("input_day3").useLines { it.toList() }
    println("From input we get: ${distanceToClosestIntersection(Pair(input[0], input[1]))}")
}

fun distanceToClosestIntersection(input: Pair<String, String>): Int {
    val firstWirePath = getPath(input.first)
    val secondWirePath = getPath(input.second)

    return firstWirePath
        .drop(1)
        .toSet().intersect(secondWirePath.toSet())
        .map { it.first.absoluteValue + it.second.absoluteValue }
        .reduce { acc, i -> if (i < acc) i else acc }
}

enum class Direction {
    L, R, U, D
}

fun getPath(input: String): List<Pair<Int, Int>> {
    val path = mutableListOf(Pair(0, 0))
    return input.split(",").flatMapTo(path) {
        plotPath(
            path.last(),
            it.drop(1).toInt(),
            Direction.valueOf(it[0].toString())
        )
    }
}

fun plotPath(startLocation: Pair<Int, Int>, distance: Int, direction: Direction): List<Pair<Int, Int>> {
    var counterX = startLocation.first
    var counterY = startLocation.second
    return when (direction) {
        Direction.R -> List(distance) { Pair(++counterX, counterY) }
        Direction.L -> List(distance) { Pair(--counterX, counterY) }
        Direction.U -> List(distance) { Pair(counterX, ++counterY) }
        Direction.D -> List(distance) { Pair(counterX, --counterY) }
    }
}
