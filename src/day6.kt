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