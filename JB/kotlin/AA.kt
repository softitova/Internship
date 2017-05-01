import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

fun main(args: Array<String>) {
    val `in` = BufferedReader(FileReader("input.txt"))
    val out = PrintWriter(File("output.txt"))
    val n = Integer.parseInt(`in`.readLine())
    var st: StringTokenizer
    for (i in 0 until n) {
        st = StringTokenizer(`in`.readLine())
        val res = d(st.nextToken().toCharArray(), Integer.parseInt(st.nextToken()));
        out.println("Case #" + (i + 1) + ": " + if (res < 0) "IMPOSSIBLE" else res);
    }
    out.close()
}

fun d(s: CharArray, k: Int): Int {
    var cnt: Int = 0;

    for ((index, value) in s.withIndex()) {
        if (value == '-') {
            if (index + k > s.size) {
                return -1;
            }
            for (j in index until index + k) {
                s.set(j, if (s.get(j) == '-') '+' else '-')
            }
            cnt++;
        }
    }
    return cnt;
}
