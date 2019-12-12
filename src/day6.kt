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
//    println("Length of input list: ${input.size}")
    println("Using input we get: ${getSumOfOrbits(input)}")
}

fun getSumOfOrbits(data: List<String>): Int {
    val pairs = data.map {
        val objects = it.split(")")
        OrbitingObject(objects.last(), objects.first())
    }
    println("Length of pairs list: ${pairs.size}")
    var distancesToCom = mapOf<String, Int>()
    for (orbitingObject in pairs) {
        distancesToCom = findPathLengthsToCom(orbitingObject, distancesToCom, pairs)
    }
    println(distancesToCom)
    println("Number of keys in the map (shouldn't this match the length of the input list?): ${distancesToCom.keys.size}")
    return distancesToCom.values.sum()
}

data class OrbitingObject(val name: String, val parent: String)

fun findPathLengthsToCom(orbitingObject: OrbitingObject,
                         distancesToCom: Map<String, Int>,
                         allOrbits: List<OrbitingObject>): Map<String, Int> {
    if (orbitingObject.parent == "COM") return distancesToCom + Pair(orbitingObject.name, 1)
    if (distancesToCom.containsKey(orbitingObject.name)) return distancesToCom
    if (distancesToCom.containsKey(orbitingObject.parent)) {
        return distancesToCom + Pair(orbitingObject.name, distancesToCom.getValue(orbitingObject.parent) + 1)
    }
    return findPathLengthsToCom(allOrbits.filter { it.name == orbitingObject.parent }.first(), distancesToCom, allOrbits)
}