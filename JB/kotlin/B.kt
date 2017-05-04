import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter

internal var ans: Long = 0
internal var powTen = LongArray(19)
internal var Ones = LongArray(19)


fun main(args: Array<String>) {
    val reader = BufferedReader(FileReader("input.txt"))
    val writer = PrintWriter(File("output.txt"))
    val n = reader.readLine().toInt()
    powTen[0] = 1
    Ones[0] = 1
    powers()
    for (i in 0..n - 1) {
        ans = 0
        count(reader.readLine(), 0)
        writer.println("Case #" + (i + 1) + ": " + ans)
    }
    writer.close()
}

fun count(strVal: String, i: Int) {
    if (i == strVal.length) {
        return
    }
    val value = strVal.toLong()
    var tmp = Ones[strVal.length - i - 1]
    var res: Long = 0
    var ii = 0
    while (ans + tmp <= value && ii < 9) {
        res = tmp
        ii++
        tmp += Ones[strVal.length - i - 1]
    }
    ans = (ans + res) / powTen[strVal.length - i - 1] * powTen[strVal.length - i - 1]
    count(strVal, i + 1)
}

fun powers() {
    for (i in 1..18) {
        powTen[i] = 10 * powTen[i - 1]
        Ones[i] = 10 * Ones[i - 1] + 1
    }
}


