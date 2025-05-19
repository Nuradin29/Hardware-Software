object LCD {
    private const val LINES = 2
    private const val COLS = 16
    private const val SERIAL_INTERFACE = true

    private fun writeNibbleSerial(rs: Boolean, data: Int) {
        val valToSend = (if (rs) 0x10 else 0) or (data and 0xF)
        SerialEmitter.send(SerialEmitter.Destination.LCD, valToSend, 5)
        SerialEmitter.pulseEnable()
        Thread.sleep(2)
    }

    private fun writeNibble(rs: Boolean, data: Int) {
        if (SERIAL_INTERFACE) {
            writeNibbleSerial(rs, data)
        } else {
            throw UnsupportedOperationException("Parallel interface not implemented")
        }
    }

    private fun writeByte(rs: Boolean, data: Int) {
        writeNibble(rs, (data shr 4) and 0xF)
        writeNibble(rs, data and 0xF)
        Thread.sleep(2)
    }

    private fun writeCMD(cmd: Int) {
        writeByte(false, cmd)
        Thread.sleep(5)
    }

    private fun writeDATA(data: Int) {
        writeByte(true, data)
        Thread.sleep(2)
    }

    fun init() {
        Thread.sleep(50)
        writeCMD(0x30)
        Thread.sleep(5)
        writeCMD(0x30)
        Thread.sleep(5)
        writeCMD(0x30)
        Thread.sleep(5)
        writeCMD(0x20)
        Thread.sleep(5)
        writeCMD(0x28)
        writeCMD(0x0C)
        writeCMD(0x06)
        clear()
    }

    fun clear() {
        writeCMD(0x01)
        Thread.sleep(100)
    }

    fun writeChar(c: Char) {
        writeDATA(c.code)
    }

    fun writeString(text: String) {
        text.forEach { writeChar(it) }
    }

    fun cursor(line: Int, column: Int) {
        val addr = if (line == 0) column else 0x40 + column
        writeCMD(0x80 or addr)
    }
}
