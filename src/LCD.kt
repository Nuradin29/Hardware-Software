object LCD {
    private const val LINES = 2
    private const val COLS = 16
    private const val SERIAL_INTERFACE = true

    private fun log(msg: String) = println("[LCD] $msg")

    private fun writeNibbleSerial(rs: Boolean, data: Int) {
        val valToSend = (if (rs) 0x10 else 0) or (data and 0xF)
        log("writeNibbleSerial(rs=$rs, data=0x${data.toString(16)}) valToSend=0x${valToSend.toString(16)}")
        SerialEmitter.send(SerialEmitter.Destination.LCD, valToSend, 5)
        SerialEmitter.pulseEnable()
        Thread.sleep(2) // küçük bekleme
    }

    private fun writeNibble(rs: Boolean, data: Int) {
        if (SERIAL_INTERFACE) {
            writeNibbleSerial(rs, data)
        } else {
            // Paralel arayüz varsa buraya implementasyon eklenebilir
            throw UnsupportedOperationException("Parallel interface not implemented")
        }
    }

    private fun writeByte(rs: Boolean, data: Int) {
        log("writeByte(rs=$rs, data=0x${data.toString(16)})")
        writeNibble(rs, (data shr 4) and 0xF)
        writeNibble(rs, data and 0xF)
        Thread.sleep(2)
    }

    private fun writeCMD(cmd: Int) {
        log("writeCMD(0x${cmd.toString(16)})")
        writeByte(false, cmd)
        Thread.sleep(5)
    }

    private fun writeDATA(data: Int) {
        log("writeDATA(0x${data.toString(16)})")
        writeByte(true, data)
        Thread.sleep(2)
    }

    fun init() {
        log("LCD init started")
        Thread.sleep(50)
        // Başlangıç komutları, 4-bit mod ayarı vs.
        writeCMD(0x30)
        Thread.sleep(5)
        writeCMD(0x30)
        Thread.sleep(5)
        writeCMD(0x30)
        Thread.sleep(5)
        writeCMD(0x20)  // 4-bit mode
        Thread.sleep(5)
        writeCMD(0x28)  // 2 satır, 5x8 matrix
        writeCMD(0x0C)  // Display ON, cursor OFF, blink OFF
        writeCMD(0x06)  // Cursor artışı sağa
        clear()
        log("LCD init finished")
    }

    fun clear() {
        log("clear display")
        writeCMD(0x01)  // Ekranı temizle
        Thread.sleep(100)
    }

    fun writeChar(c: Char) {
        log("write char '$c'")
        writeDATA(c.code)
    }

    fun writeString(text: String) {
        log("write string \"$text\"")
        text.forEach { writeChar(it) }
    }

    fun cursor(line: Int, column: Int) {
        val addr = if (line == 0) column else 0x40 + column
        log("set cursor line=$line, column=$column (addr=0x${addr.toString(16)})")
        writeCMD(0x80 or addr)
    }
}
