object LCD {
    private const val SERIAL_INTERFACE = true

    private const val ENTRY_MODE_SET = 0x06
    private const val SET_8_BITS_2_LINES = 0x03
    private const val SET_4_BITS_2_LINES = 0x02
    private const val DISPLAY_CLEAR = 0x01
    private const val DISPLAY_OFF = 0x08
    private const val DISPLAY_ON = 0x0F
    private const val LINE_FONT = 0x28
    private const val ON = 0x01
    private const val OFF = 0x00

    private fun writeNibbleSerial(rs: Boolean, data: Int) {
        val valToSend = (data and 0x0F) shl 1 or if (rs) 1 else 0
        println("Gönderilen nibble: ${valToSend.toString(2).padStart(5, '0')}")

        SerialEmitter.send(SerialEmitter.Destination.LCD, valToSend, 5)
        Thread.sleep(2)
    }

    private fun writeNibble(rs: Boolean, data: Int) {
        if (SERIAL_INTERFACE) writeNibbleSerial(rs, data)
        else throw UnsupportedOperationException("Parallel interface not supported")
    }

    private fun writeByte(rs: Boolean, data: Int) {
        writeNibble(rs, (data shr 4) and 0x0F)
        writeNibble(rs, data and 0x0F)
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
        Thread.sleep(20)
        writeNibble(false, SET_8_BITS_2_LINES)
        Thread.sleep(5)
        writeNibble(false, SET_8_BITS_2_LINES)
        Thread.sleep(1)
        writeNibble(false, SET_8_BITS_2_LINES)
        writeNibble(false, SET_4_BITS_2_LINES)

        writeCMD(LINE_FONT)
        writeCMD(DISPLAY_OFF)
        clear()
        writeCMD(ENTRY_MODE_SET)
        writeCMD(DISPLAY_ON)
        Thread.sleep(2)
    }

    fun clear() {
        writeCMD(DISPLAY_CLEAR)
    }

    fun write(c: Char) {
        if (c != 0.toChar()) writeDATA(c.code)
    }

    fun writeString(text: String) {
        text.forEach { write(it) }
    }

    fun cursor(line: Int, column: Int) {
        val addr = if (line == 0) column else 0x40 + column
        writeCMD(0x80 or addr)
    }
}


fun main() {
    HAL.init()
    SerialEmitter.init()
    LCD.init()
    LCD.cursor(0, 0)
    LCD.writeString("Roulette Game")
    LCD.cursor(1, 0)
    LCD.writeString("1 2 3 $0")
}
