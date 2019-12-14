import java.io.File

fun main(args: Array<String>) {
    val testData = "COM)B\n" +
                    "B)C\n" +
                    "C)D\n" +
                    "D)E\n" +
                    "E)F\n" +
                    "B)G\n" +
                    "G)H\n" +
                    "D)I\n" +
                    "E)J\n" +
                    "J)K\n" +
                    "K)L"
    println("Test data passes: ${getSumOfOrbits(testData.split("\n")) == 42}")
    val input = File("input_day6").useLines { it.toList() }
    println("Using input we get: ${getSumOfOrbits(input)}")

    val pt2TestData = "COM)B\n" +
            "B)C\n" +
            "C)D\n" +
            "D)E\n" +
            "E)F\n" +
            "B)G\n" +
            "G)H\n" +
            "D)I\n" +
            "E)J\n" +
            "J)K\n" +
            "K)L\n" +
            "K)YOU\n" +
            "I)SAN"
    println("Part 2 test data passes: ${findPathLengthTo("SAN", "YOU", pt2TestData.split("\n")) == 4}")
    println(findPathLengthTo("SAN", "YOU", pt2TestData.split("\n")))
    println("Using input we get: ${findPathLengthTo("SAN", "YOU", input)}")
}

fun getSumOfOrbits(data: List<String>): Int {
    val orbitingObjects = data.map {
        val objects = it.split(")")
        OrbitingObject(objects.last(), objects.first())
    }
    return findPathLengthsToCom(orbitingObjects, emptyMap()).values.sum()
}

data class OrbitingObject(val name: String, val parent: String)

tailrec fun findPathLengthsToCom(
        remainingObjects: List<OrbitingObject>,
        distancesToCom: Map<String, Int>
    ): Map<String, Int> {
    if (remainingObjects.isEmpty()) return distancesToCom
    val orbitingObject = remainingObjects.first()
    if (orbitingObject.parent == "COM") {
        return findPathLengthsToCom(remainingObjects.drop(1),
                distancesToCom + Pair(orbitingObject.name, 1))
    }
    if (distancesToCom.containsKey(orbitingObject.parent)) {
        return findPathLengthsToCom(remainingObjects.drop(1), distancesToCom + Pair(orbitingObject.name,
                distancesToCom.getValue(orbitingObject.parent) + 1))
    }
    return findPathLengthsToCom(remainingObjects.drop(1) + listOf(orbitingObject), distancesToCom)
}

fun findPathLengthTo(target: String, origin: String, graph: List<String>): Int {
    val pathFromTargetToCom: List<OrbitingObject> = getPathToObject(target, "COM", graph)
    val pathFromOriginToCom: List<OrbitingObject> = getPathToObject(origin, "COM", graph)
    val firstCommonObject = pathFromTargetToCom.first { pathFromOriginToCom.contains(it) }
    return pathFromOriginToCom.indexOf(firstCommonObject) + pathFromTargetToCom.indexOf(firstCommonObject) - 2
}

fun getPathToObject(start: String, targetParent: String, graph: List<String>): List<OrbitingObject> {
    val orbitingObjects = graph.map {
        val split = it.split(")")
        OrbitingObject(split[1], split[0])
    }
    val firstObject = listOf(orbitingObjects.first { it.name == start })
    return getPathToObjectHelper(orbitingObjects.subtract(firstObject).toList(), targetParent, firstObject)
}

tailrec fun getPathToObjectHelper(remainingOrbitingObjects: List<OrbitingObject>,
                          targetParentName: String,
                          acc: List<OrbitingObject>): List<OrbitingObject> {
    if (acc.find { it.parent == targetParentName } != null) return acc
    val orbitingObject = remainingOrbitingObjects.first()
    if (orbitingObject.name == acc.last().parent) return getPathToObjectHelper(
            remainingOrbitingObjects.drop(1),
            targetParentName,
            acc + listOf(orbitingObject))
    return getPathToObjectHelper(
            remainingOrbitingObjects.drop(1) + listOf(orbitingObject),
            targetParentName,
            acc)
}