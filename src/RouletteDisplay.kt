object RouletteDisplay {
    private val digits = IntArray(6) { 0 }
    private val SEG_MASKS = listOf(0x01, 0x02, 0x04, 0x08, 0x10, 0x20)
    private val SEG_CODES = mapOf(
        0 to 0b00111111, 1 to 0b00000110, 2 to 0b01011011, 3 to 0b01001111,
        4 to 0b01100110, 5 to 0b01101101, 6 to 0b01111101, 7 to 0b00000111,
        8 to 0b01111111, 9 to 0b01101111
    )

    fun init() {
        clear()
        println("Roulette Display initialized.")
    }

    fun setValue(value: Int) {
        val s = value.toString().padStart(6, '0')
        s.forEachIndexed { i, ch -> digits[i] = ch.digitToInt() }
        update()
    }

    fun clear() {
        for (i in digits.indices) digits[i] = 0
        update()
    }

    private fun update() {
        println("\n7 Segment Display:")
        println(digits.joinToString(""))
        // Her digit için bir port uygula
        for (i in digits.indices) {
            val code = SEG_CODES[digits[i]] ?: 0
            val mask = SEG_MASKS[i]
            // Sadece ilgili digit hattını seç ve kodu alt 7 bite koy:
            // (mask << 7) | code   → senin devre bağlantına göre uyarlaman gerekebilir
            val portVal = (mask shl 7) or code
            HAL.writeBits((mask shl 7) or 0x7F, portVal)
            println(" Digit ${i+1}='${digits[i]}', segMask=0x${mask.toString(16)}, code=0b${code.toString(2)}, port=0x${portVal.toString(16)}")
        }
    }
}
