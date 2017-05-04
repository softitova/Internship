import javafx.util.Pair
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

fun main(args: Array<String>) {
    val reader = BufferedReader(FileReader("input.txt"))
    val writer = PrintWriter(File("output.txt"))
    val t = Integer.parseInt(reader.readLine())
    var st: StringTokenizer
    for (i in 0..t - 1) {
        st = StringTokenizer(reader.readLine())
        val p = count(st.nextToken().toLong(), st.nextToken().toLong())
        writer.println("Case #" + (i + 1) + ": " + p.key + " " + p.value);
    }
    writer.close()
}

private fun count(n: Long, k: Long): Pair<Long, Long> {
    var mk = k
    val m = TreeMap<Long, Long>()
    m.put(-n, 1L)

    while (true) {
        val p = m.firstEntry()
        m.remove(p.key)
        if (p.value < mk) {
            m.merge(-(-p.key / 2), p.value) { u, v -> u + v }
            m.merge(-((-p.key - 1) / 2), p.value) { u, v -> u + v }
            mk -= p.value
        } else {
            return Pair(-p.key / 2, (-p.key - 1) / 2);
        }
    }
}