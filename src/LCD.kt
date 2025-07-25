object LCD {
    // LCD Command Constants
    private const val CLEAR_DISPLAY = 0x01
    private const val SET_4_BIT_MODE = 0x02
    private const val DISPLAY_ON_CURSOR_ON = 0x0F
    private const val LINE_FONT_5X8 = 0x28
    private const val ENTRY_MODE = 0x06

    // Send data to LCD via SerialEmitter
    private fun sendToLCD(isData: Boolean, value: Int) {
        val packet = (value and 0x0F) shl 1 or if (isData) 1 else 0
        SerialEmitter.send(SerialEmitter.Destination.LCD, packet, 5)
        Thread.sleep(2)
    }

    // Send a full byte by splitting into two 4-bit operations
    private fun sendByte(isData: Boolean, value: Int) {
        // Send high nibble (bits 4-7)
        sendToLCD(isData, (value shr 4) and 0x0F)
        // Send low nibble (bits 0-3)
        sendToLCD(isData, value and 0x0F)
        Thread.sleep(2)
    }

    // Send a command to the LCD
    private fun sendCommand(cmd: Int) {
        sendByte(false, cmd)
        Thread.sleep(5)
    }

    // Send a data byte to the LCD
    private fun sendData(data: Int) {
        sendByte(true, data)
        Thread.sleep(2)
    }

    // Initialize the LCD display
    fun init() {
        Thread.sleep(20)
        // Standard LCD initialization sequence for 4-bit mode
        sendToLCD(false, 0x03) // 8-bit mode attempt 1
        Thread.sleep(5)
        sendToLCD(false, 0x03) // 8-bit mode attempt 2
        Thread.sleep(1)
        sendToLCD(false, 0x03) // 8-bit mode attempt 3
        sendToLCD(false, SET_4_BIT_MODE) // Set 4-bit mode

        // Configure the display
        sendCommand(LINE_FONT_5X8)    // 2 lines, 5x8 font
        sendCommand(0x08)             // Display off
        clear()                       // Clear display
        sendCommand(ENTRY_MODE)       // Entry mode set
        sendCommand(DISPLAY_ON_CURSOR_ON)  // Display on, cursor on
        Thread.sleep(2)
    }

    fun clear() {
        sendCommand(CLEAR_DISPLAY)
        Thread.sleep(5) // Clear command needs extra time
    }

    fun write(c: Char) {
        if (c != 0.toChar()) sendData(c.code)
    }

    fun writeString(text: String) {
        text.forEach { write(it) }
    }

    fun cursor(line: Int, column: Int) {
        // Calculate address: first line starts at 0x00, second at 0x40
        val addr = if (line == 0) column else 0x40 + column
        sendCommand(0x80 or addr) // Set DDRAM address command
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
