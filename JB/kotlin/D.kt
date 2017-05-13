import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*


private var nn: Int = 0
private var no_need_x_diags: Array<BooleanArray>? = null
private val reader = BufferedReader(FileReader("input.txt"))
private val out = PrintWriter(File("output.txt"))

fun main(args: Array<String>) {
    val st = StringTokenizer(reader.readLine())
    val n = st.nextToken().toInt()
    for (i in 0..n - 1) {
        out.print("Case #" + (i + 1) + ": ")
        solve(st)
    }
    out.close()
}

private fun solve(st: StringTokenizer) {
    var st = st
    st = StringTokenizer(reader.readLine())
    val n = st.nextToken().toInt()
    val m = st.nextToken().toInt()
    val need_x_diag = Array(n) { BooleanArray(n) }
    val need_plus_line = Array(n) { BooleanArray(n) }
    val changed = Array(n) { BooleanArray(n) }

    var model: String
    var r: Int
    var c: Int
    for (i in 0..m - 1) {
        st = StringTokenizer(reader.readLine())
        model = st.nextToken()
        r = st.nextToken().toInt() - 1
        c = st.nextToken().toInt() - 1
        when (model) {
            "o" -> {
                need_plus_line[r][c] = true
                need_x_diag[r][c] = true
            }
            "x" -> need_plus_line[r][c] = true
            "+" -> need_x_diag[r][c] = true
        }
    }


    lines(need_plus_line, n, changed)

    nn = 2 * n - 1
    val p = IntArray(nn)
    no_need_x_diags = Array(nn) { BooleanArray(nn) }
    Arrays.fill(p, -1)


    diagonals(need_x_diag, n);

    val z = BooleanArray(nn)
    (0..nn - 1).forEach { i ->
        Arrays.fill(z, false)
        dfs(i, z, p)
    }

    (0..nn - 1).forEach { i ->
        if (p[i] >= 0) {
            val i1 = (p[i] + i - (n - 1)) / 2
            val j1 = p[i] - i1
            need_x_diag[i1][j1] = true
            changed[i1][j1] = true
        }
    }
    var ans1 = 0
    var ans2 = 0
    (0..n - 1).forEach { i ->
        (0..n - 1).forEach { j ->
            if (changed[i][j]) ans2++
            if (need_plus_line[i][j]) ans1++
            if (need_x_diag[i][j]) ans1++
        }
    }
    print_ans(ans1, ans2, need_plus_line, need_x_diag, n, changed)
}

private fun lines(need_plus_line: Array<BooleanArray>, n: Int, changed: Array<BooleanArray>) {
    var res = n
    val r = BooleanArray(n)
    val c = BooleanArray(n)
    (0..n - 1).forEach { i ->
        (0..n - 1).forEach { j ->
            if (need_plus_line[i][j]) {
                r[i] = true
                c[j] = true
                res--
            }
        }
    }
    var row = 0
    var col = 0
    for (i in 0..res - 1) {
        while (r[row]) row++
        while (c[col]) col++
        need_plus_line[row][col] = true
        changed[row++][col++] = true
    }
}

private fun diagonals(need_x_diag: Array<BooleanArray>, n: Int) {
    val dl = BooleanArray(nn)
    val dr = BooleanArray(nn)
    (0..n - 1).forEach { i ->
        (0..n - 1).forEach { j ->
            if (need_x_diag[i][j]) {
                dr[i - j + n - 1] = true
                dl[i + j] = true
            }
        }
    }
    (0..n - 1).forEach { i ->
        (0..n - 1).forEach { j ->
            val i1 = i + j
            val j1 = i - j + n - 1
            if (!dl[i1] && !dr[j1]) {
                no_need_x_diags!![i1][j1] = true
            }
        }
    }
}

private fun print_ans(ans1: Int, ans2: Int, need_plus_line: Array<BooleanArray>, need_x_diag: Array<BooleanArray>, n: Int, changed: Array<BooleanArray>) {
    val res = StringBuilder().append(ans1).append(" ").append(ans2)
    (0..n - 1).forEach { i ->
        (0..n - 1).forEach { j ->
            if (changed[i][j]) {
                res.append("\n")
                if (need_x_diag[i][j] && need_plus_line[i][j]) {
                    res.append("o")
                } else if (need_plus_line[i][j]) {
                    res.append("x")
                } else if (need_x_diag[i][j]) {
                    res.append("+")
                }
                res.append(" ").append(i + 1).append(" ").append(j + 1)
            }
        }
    }
    out.println(res.toString());
}

private fun dfs(i: Int, z: BooleanArray, p: IntArray): Boolean {
    if (z[i]) return false
    z[i] = true
    (0..nn - 1).forEach { j ->
        if (no_need_x_diags!![i][j]) {
            if (p[j] == -1
                    || dfs(p[j], z, p)) {
                p[j] = i
                return true
            }
        }
    }
    return false
}
