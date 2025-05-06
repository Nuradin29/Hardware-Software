object LCD {
    private const val LINES = 2
    private const val COLS = 16

    fun init() {
        clear()
    }

    fun clear() {
        // Komut 0x01: LCD clear ekran komutu
        sendCommand(0x01)
    }

    fun cursor(line: Int, column: Int) {
        // LCD DDRAM adresleri: line 0 -> 0x00, line 1 -> 0x40
        val address = if (line == 0) column else 0x40 + column
        sendCommand(0x80 or address) // 0x80 komutu: Set DDRAM address
    }

    fun write(c: Char) {
        sendData(c.code)
    }

    fun write(text: String) {
        for (ch in text) {
            write(ch)
        }
    }

    // Yardımcı: Komut göndermek için
    private fun sendCommand(data: Int) {
        SerialEmitter.send(SerialEmitter.Destination.LCD, data, 8)
    }

    // Yardımcı: Veri göndermek için
    private fun sendData(data: Int) {
        // RS = 1 için veri olduğu varsayılıyor (adresleme vs donanım tarafında ayrılır)
        SerialEmitter.send(SerialEmitter.Destination.LCD, data or 0x100, 9)
    }
}
