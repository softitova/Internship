import java.io.*

internal var ans: Long = 0
internal var powTen = LongArray(19)
internal var Ones = LongArray(19)

fun count(strVal: String, i: Int) {
    if (i == strVal.length) {
        return
    }
    val `val` = java.lang.Long.parseLong(strVal)
    var tmp = Ones[strVal.length - i - 1]

    var res: Long = 0
    var ii = 0
    while (ans + tmp <= `val` && ii < 9) {
        res = tmp
        ii++
        tmp += Ones[strVal.length - i - 1]
    }
    ans += res
    ans = ans / powTen[strVal.length - i - 1] * powTen[strVal.length - i - 1]
    count(strVal, i + 1)
}

fun powers() {
    for (i in 1..18) {
        powTen[i] = 10 * powTen[i - 1]
        Ones[i] = 10 * Ones[i - 1] + 1
    }
}

fun main(args: Array<String>) {
    val `in` = BufferedReader(FileReader("input.txt"))
    val out = PrintWriter(File("output.txt"))
    val n = Integer.parseInt(`in`.readLine())
    powTen[0] = 1
    Ones[0] = 1
    powers()
    for (i in 0..n - 1) {
        ans = 0
        val `val` = `in`.readLine()
        count(`val`, 0)
        out.println("Case #" + (i + 1) + ": " + ans)
    }
    out.close()
}

