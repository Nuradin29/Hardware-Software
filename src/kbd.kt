object KBD {
    const val NONE = 0.toChar()

    private val keyMap = arrayOf(
        charArrayOf('1', '2', '3', 'A'),
        charArrayOf('4', '5', '6', 'B'),
        charArrayOf('7', '8', '9', 'C'),
        charArrayOf('*', '0', '#', 'D')
    )

    private val rowMasks = intArrayOf(0x01, 0x02, 0x04, 0x08) // R0-R3 = 0000 1111
    private val colMasks = intArrayOf(0x10, 0x20, 0x40, 0x80) // C0-C3 = 1111 0000

    private var lastKeyPressed: Char = NONE

    fun init() {
        // Row pinlerini HIGH yap (girişe çekiyoruz)
        HAL.writeBits(0x0F, 0x0F)
    }

    fun getKey(): Char {
        for (row in 0..3) {
            HAL.clearBits(rowMasks[row]) // Satırı 0 yaparak aktif ediyoruz
            for (col in 0..3) {
                if (!HAL.isBit(colMasks[col])) { // Sütun 0 olduysa, tuş basıldı
                    val key = keyMap[row][col]
                    HAL.setBits(rowMasks[row]) // Satırı eski haline getir

                    if (key != lastKeyPressed) {
                        lastKeyPressed = key
                        waitKeyRelease()
                        return key
                    }
                    return NONE
                }
            }
            HAL.setBits(rowMasks[row])
        }

        lastKeyPressed = NONE
        return NONE
    }

    fun waitKey(timeout: Long): Char {
        val start = System.currentTimeMillis()
        while (System.currentTimeMillis() - start < timeout) {
            val key = getKey()
            if (key != NONE) return key
        }
        return NONE
    }

    private fun waitKeyRelease() {
        while (true) {
            var keyStillPressed = false
            for (row in 0..3) {
                HAL.clearBits(rowMasks[row])
                for (col in 0..3) {
                    if (!HAL.isBit(colMasks[col])) keyStillPressed = true
                }
                HAL.setBits(rowMasks[row])
            }
            if (!keyStillPressed) break
            Thread.sleep(10)
        }
    }
}
