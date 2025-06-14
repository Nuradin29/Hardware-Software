object RouletteDisplay {
    private val segmentMap = mapOf(
        '0' to 0b11110,
        '1' to 0b00110,
        '2' to 0b11011,
        '3' to 0b11101,
        '4' to 0b01101,
        '5' to 0b10111,
        '6' to 0b10111,
        '7' to 0b11100,
        '8' to 0b11111,
        '9' to 0b11101,
        'A' to 0b11111,
        'B' to 0b00111,
        'C' to 0b10011,
        'D' to 0b01111
    )

    private val segmentSequence = listOf(
        0b01000, // d
        0b00010, // b
        0b00100, // c
        0b00001, // a
        0b10000, // e
        0b00011, // f
        0b01000  // tekrar d
    )

    fun init() {
        for (i in 0..5) writeToDisplay(i, 0)
    }

    fun writeToDisplay(display: Int, segmentBits: Int) {
        val cmd = display and 0b111
        val data = segmentBits and 0b11111
        val payload = (data shl 3) or cmd
        SerialEmitter.send(SerialEmitter.Destination.ROULETTE, payload, 8)
    }

    fun showChar(c: Char) {
        val bits = segmentMap[c.uppercaseChar()] ?: 0
        for (i in 0..5) {
            writeToDisplay(i, bits)
        }
    }

    fun setValue(segmentBits: Int) {
        for (i in 0..5) {
            writeToDisplay(i, segmentBits)
        }
    }

    fun showResult(value: Int) {
        val c = when (value) {
            in 0..9 -> '0' + value
            10 -> 'A'
            11 -> 'B'
            12 -> 'C'
            13 -> 'D'
            else -> ' '
        }
        showChar(c)
    }

    fun animation() {
        repeat(2) {
            for (pattern in segmentSequence) {
                for (i in 0..5) {
                    for (j in 0..5) {
                        val seg = if (j == i) pattern else 0
                        writeToDisplay(j, seg)
                    }
                    Thread.sleep(100)
                }
            }
        }
    }

    fun off(clear: Boolean = true) {
        if (clear) {
            for (i in 0..5) writeToDisplay(i, 0)
        }
    }
}
fun main() {
    HAL.init()
    SerialEmitter.init()
    LCD.init()
    RouletteDisplay.init()

    val char = '8'
    LCD.clear()
    LCD.writeString("Testing $char")

    RouletteDisplay.showChar(char)

    println("Gönderildi: $char")
}
