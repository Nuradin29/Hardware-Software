object RouletteDisplay {
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
        // Tüm display'lere 0 gönder (kapalı hale getir)
        for (i in 0..5) writeToDisplay(i, 0)
    }

    fun setValue(segmentBits: Int) {
        for (i in 0..5) {
            writeToDisplay(i, segmentBits)
        }
    }

    private fun writeToDisplay(display: Int, segmentBits: Int) {
        val cmd = display and 0b111
        val data = segmentBits and 0b11111
        val payload = (cmd shl 5) or data
        SerialEmitter.send(SerialEmitter.Destination.ROULETTE, payload, 8)
    }

    fun animation() {
        repeat(3) { // 3 tam döngü yap
            for (pattern in segmentSequence) {
                for (i in 0..5) {
                    writeToDisplay(i, pattern)
                }
                Thread.sleep(150)
            }
        }
    }

    fun off(clear: Boolean) {
        if (clear) {
            for (i in 0..5) {
                writeToDisplay(i, 0)
            }
        }
    }

    fun showResult(value: Int) {
        val shown = when (value) {
            in 0..9 -> value  // sayılar
            10 -> 0b00001     // 'A'
            11 -> 0b00010     // 'B'
            12 -> 0b00100     // 'C'
            13 -> 0b01000     // 'D'
            else -> 0
        }
        setValue(shown)
    }
}

fun main() {
    println("🎰 Roulette Başlatılıyor...")

    HAL.init()
    SerialEmitter.init()
    LCD.init()

    LCD.cursor(0, 0)
    LCD.writeString("Roulette Game")

    // Başlangıç: tüm display'leri sıfırla
    RouletteDisplay.init()

    // Animasyon: d → b → c → a → e → f → d
    println("▶️ Animasyon Başlıyor...")
    RouletteDisplay.animation()

    // Sonuç belirle
    val result = (0..13).random()
    println("🎯 Sonuç: $result")
    RouletteDisplay.showResult(result)
}
