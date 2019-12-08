fun main(args: Array<String>) {
    println((356261..846303).filter {
        val numChars = it.toString().toCharArray()
        hasConsecutiveDigits(numChars) && neverDecreases(numChars)
        }.size)
}

fun hasConsecutiveDigits(numStr: CharArray): Boolean {
    return numStr.foldIndexed(false) {idx, acc, ch ->
        if (idx < numStr.size - 1) ch == numStr[idx + 1] || acc
        else acc
    }
}

fun neverDecreases(numStr: CharArray): Boolean {
    return numStr.foldIndexed(true) {idx, acc, ch ->
        if (idx < numStr.size - 1) ch.toInt() <= numStr[idx + 1].toInt() && acc
        else acc
    }
}