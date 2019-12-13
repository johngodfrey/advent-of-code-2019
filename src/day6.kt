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
    var orbitingObjects = data.map {
        val objects = it.split(")")
        OrbitingObject(objects.last(), objects.first())
    }
    var distancesToCom = mapOf<String, Int>()
    while (orbitingObjects.isNotEmpty()) {
        val result = findPathLengthsToCom(distancesToCom, orbitingObjects)
        distancesToCom = result.first
        orbitingObjects = result.second
    }
    return distancesToCom.values.sum()
}

data class OrbitingObject(val name: String, val parent: String)

fun findPathLengthsToCom(distancesToCom: Map<String, Int>,
                         remainingObjects: List<OrbitingObject>): Pair<Map<String, Int>, List<OrbitingObject>> {
    val orbitingObject = remainingObjects.first()
    if (orbitingObject.parent == "COM") {
        return Pair(distancesToCom + Pair(orbitingObject.name, 1), remainingObjects.drop(1))
    }
    if (distancesToCom.containsKey(orbitingObject.parent)) {
        return Pair(distancesToCom + Pair(orbitingObject.name, distancesToCom.getValue(orbitingObject.parent) + 1),
                remainingObjects.drop(1))
    }
    return Pair(distancesToCom, remainingObjects.drop(1) + listOf(orbitingObject))
}