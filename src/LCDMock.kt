object LCDMock {
    private val lines = 2
    private val cols = 16
    private val buffer = Array(lines) { CharArray(cols) { ' ' } }
    private var cursorLine = 0
    private var cursorCol = 0

    fun clear() {
        for (i in buffer.indices) {
            for (j in buffer[i].indices) {
                buffer[i][j] = ' '
            }
        }
        cursorLine = 0
        cursorCol = 0
    }

    fun cursor(line: Int, col: Int) {
        cursorLine = line.coerceIn(0, lines - 1)
        cursorCol = col.coerceIn(0, cols - 1)
    }

    fun write(text: String) {
        var col = cursorCol
        for (c in text) {
            if (col >= cols) break
            buffer[cursorLine][col] = c
            col++
        }
    }

    fun printBuffer() {
        println("---- LCD DISPLAY ----")
        buffer.forEach { println(it.concatToString()) }
        println("---------------------")
    }
}
