fun main(args: Array<String>) {
    println("First test passes: ${testSingly("112233")}")
    println("Second test fails: ${!testSingly("123444")}")
    println("Third test passes: ${testSingly("111122")}")
    println((356261..846303).filter {
        val numChars = it.toString().toCharArray()
        hasOnlyTwoConsecutiveDigits(numChars) && neverDecreases(numChars)
        }.size)
}

fun testSingly(numStr: String) : Boolean {
    val numChars = numStr.toCharArray()
    return hasOnlyTwoConsecutiveDigits(numChars) && neverDecreases(numChars)
}

fun hasOnlyTwoConsecutiveDigits(numStr: CharArray): Boolean {
    return numStr.foldIndexed(false) {idx, acc, ch ->
        // println("Considering $ch at index $idx, acc is $acc")
        when{
            idx == 0 -> (ch == numStr[idx + 1] && ch != numStr[idx + 2]) || acc
            idx < numStr.size - 2 -> (ch == numStr[idx + 1] && ch != numStr[idx + 2] && ch != numStr[idx - 1]) || acc
            idx == numStr.size - 2 -> ch == numStr[idx + 1] && ch != numStr[idx - 1] || acc
            else -> acc
        }
    }
}
