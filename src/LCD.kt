object LCD {
    private const val LINES = 2
    private const val COLS = 16

    fun init() {
        writeCMD(0x33)
        writeCMD(0x32)
        writeCMD(0x28)
        writeCMD(0x0C)
        writeCMD(0x06)
        clear()
    }

    private fun writeNibble(rs: Boolean, data: Int) {
        val rsBit = if (rs) 0x01 else 0x00
        val value = (data and 0xF0) or rsBit
        HAL.writeBits(0xFF, value)
        Thread.sleep(1)
    }

    private fun writeByte(rs: Boolean, data: Int) {
        writeNibble(rs, data and 0xF0)
        writeNibble(rs, (data shl 4) and 0xF0)
    }

    private fun writeCMD(data: Int) {
        writeByte(false, data)
    }

    private fun writeDATA(data: Int) {
        writeByte(true, data)
    }

    fun write(c: Char) {
        writeDATA(c.code)
    }

    fun write(text: String) {
        for (c in text) {
            write(c)
        }
    }

    fun cursor(line: Int, column: Int) {
        val pos = when (line) {
            0 -> 0x00 + column
            1 -> 0x40 + column
            else -> 0
        }
        writeCMD(0x80 or pos)
    }

    fun clear() {
        writeCMD(0x01)
        Thread.sleep(2)
    }
}
