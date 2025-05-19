import SerialEmitter.pulseEnable

object LCD {
    private const val LINES = 2
    private const val COLS = 16
    private const val SERIAL_INTERFACE = true  // Simülasyonda seri iletişim kullanılıyor

    private fun writeNibbleSerial(rs: Boolean, data: Int) {
        val valToSend = (if (rs) 0x10 else 0) or (data and 0xF)
        SerialEmitter.send(SerialEmitter.Destination.LCD, valToSend, 5)
        SerialEmitter.pulseEnable()
    }




    // Paralel 4 bit yazma (simülasyonda paralel kullanılmıyorsa devre dışı bırakabilirsin)
    private fun writeNibbleParallel(rs: Boolean, data: Int) {
        // Önce ilgili pinleri temizle
        HAL.clrBits(Masks.O0 or Masks.O1 or Masks.O2 or Masks.O3 or Masks.O4 or Masks.O7)

        if ((data and 0x1) != 0) HAL.setBits(Masks.O1)
        if ((data and 0x2) != 0) HAL.setBits(Masks.O2)
        if ((data and 0x4) != 0) HAL.setBits(Masks.O3)
        if ((data and 0x8) != 0) HAL.setBits(Masks.O4)

        // RS pinini ayarla
        if (rs) HAL.setBits(Masks.O0) else HAL.clrBits(Masks.O0)

        // Enable pini için pulse üret
        HAL.setBits(Masks.O7)
        Thread.sleep(5)
        HAL.clrBits(Masks.O7)
        Thread.sleep(5)
    }

    private fun writeNibble(rs: Boolean, data: Int) {
        if (SERIAL_INTERFACE) {
            writeNibbleSerial(rs, data)
        } else {
            writeNibbleParallel(rs, data)
        }
    }

    // 8-bit komut ya da veri için iki 4-bit nibble gönderimi
    private fun writeByte(rs: Boolean, data: Int) {
        writeNibble(rs, (data shr 4) and 0xF)  // Üst nibble
        writeNibble(rs, data and 0xF)          // Alt nibble
    }

    private fun writeCMD(data: Int) = writeByte(false, data)
    private fun writeDATA(data: Int) = writeByte(true, data)

    fun init() {
        // LCD başlangıç komutları
        writeCMD(0x28) // 4-bit, 2 satır, 5x8 font
        writeCMD(0x0C) // Display ON, Cursor OFF
        writeCMD(0x06) // Entry mode set (cursor sağa kayar)
        clear()
    }

    fun clear() {
        writeCMD(0x01) // Ekranı temizle
        Thread.sleep(100)
    }

    fun write(c: Char) = writeDATA(c.code)
    fun write(text: String) = text.forEach { write(it) }

    fun cursor(line: Int, column: Int) {
        val addr = if (line == 0) column else 0x40 + column
        writeCMD(0x80 or addr)
    }
}
