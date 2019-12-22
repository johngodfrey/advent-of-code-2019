import java.io.File

fun main(args: Array<String>) {
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
     val input = File("input_day3").useLines { it.toList() }
     println("From input we get: ${distanceToClosestIntersectionWithStepCount(Pair(input[0], input[1]))}")
}

fun distanceToClosestIntersectionWithStepCount(input: Pair<String, String>): Int {
    val firstWirePath = getPathWithStepCount(input.first)
    val secondWirePath = getPathWithStepCount(input.second)

    val crossings = firstWirePath.path
            .drop(1)
            .toSet().intersect(secondWirePath.path.toSet())

    return crossings
        .map { firstWirePath.stepCountMap.getValue(it) + secondWirePath.stepCountMap.getValue(it) }
            .sorted().first()
}

data class PathWithStepCountMap(val path: List<Pair<Int, Int>>, val stepCountMap: Map<Pair<Int, Int>, Int>)

fun getPathWithStepCount(input: String): PathWithStepCountMap {
    val path = mutableListOf(Pair(0, 0))
    input.split(",").flatMapTo(path) {
        plotPath(
            path.last(),
            it.drop(1).toInt(),
            Direction.valueOf(it[0].toString())
        )
    }
    val map = mutableMapOf<Pair<Int, Int>, Int>()
    return PathWithStepCountMap(path.mapIndexed { idx, it ->
        val key = Pair(it.first, it.second)
        if (!map.containsKey(key) || idx < map.getValue(key)) {
            map[key] = idx
        }
        key
        }, map)
}