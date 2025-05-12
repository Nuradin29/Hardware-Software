object LCD {
    private const val LINES = 2
    private const val COLS = 16
    private val buf = Array(LINES) { CharArray(COLS) { ' ' } }

    fun init() {
        clear()
        println("LCD initialized.")
    }

    fun clear() {
        for (r in buf.indices) for (c in buf[r].indices) buf[r][c] = ' '
        update()
    }

    fun write(text: String) {
        var r = 0; var c = 0
        text.forEach { ch ->
            if (ch == '\n') { r++; c = 0 }
            else if (r < LINES && c < COLS) buf[r][c++] = ch
        }
        update()
    }

    private fun update() {
        println("\nLCD Display:")
        buf.forEach { println(it.concatToString()) }
    }
}
