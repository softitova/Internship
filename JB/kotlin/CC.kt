import javafx.util.Pair
import java.io.*
import java.util.*

fun main(args: Array<String>) {
    val `in` = BufferedReader(FileReader("input.txt"))
    val out = PrintWriter(File("output.txt"))
    val t = Integer.parseInt(`in`.readLine())
    var st: StringTokenizer
    for (i in 0..t - 1) {
        st = StringTokenizer(`in`.readLine())
        val p = count(java.lang.Long.parseLong(st.nextToken()),
                java.lang.Long.parseLong(st.nextToken()))
        out.println("Case #" + (i + 1) + ": " + p.key + " " + p.value);
    }
    out.close()
}

private fun count(n: Long, k: Long): Pair<Long, Long> {
    var pow: Long = Math.pow(2.0, (Math.log10(k.toDouble()) / Math.log10(2.0)).toLong().toDouble()).toLong() - 1
    var length: Long = (n - pow) / (pow + 1)
    if (n - pow - length * (pow + 1) - (k - pow) >= 0) {
        length += 1
    }
    var max : Long = length / 2
    return Pair(max, if ((length and 1L == 0L)) max - 1 else max)
}