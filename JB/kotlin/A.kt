import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

fun main(args: Array<String>) {
    val reader = BufferedReader(FileReader("input.txt"))
    val writer = PrintWriter(File("output.txt"))
    val n = Integer.parseInt(reader.readLine())
    var st: StringTokenizer
    for (i in 0 until n) {
        st = StringTokenizer(reader.readLine())
        val res = d(st.nextToken().toCharArray(), st.nextToken().toInt());
        writer.println("Case #" + (i + 1) + ": " + if (res < 0) "IMPOSSIBLE" else res);
    }
    writer.close()
}

fun d(s: CharArray, k: Int): Int {
    var cnt: Int = 0;
    for ((index, value) in s.withIndex()) {
        if (value == '-') {
            if (index + k > s.size) {
                return -1;
            }
            for (j in index until index + k) {
                s[j] = if (s[j] == '-') '+' else '-'
            }
            cnt++;
        }
    }
    return cnt;
}
