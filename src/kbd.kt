object KBD {
    const val NONE = 0.toChar()

    private val keyMap = arrayOf(
        charArrayOf('1', '2', '3', 'A'),
        charArrayOf('4', '5', '6', 'B'),
        charArrayOf('7', '8', '9', 'C'),
        charArrayOf('*', '0', '#', 'D')
    )

    private val rowMasks = intArrayOf(0x01, 0x02, 0x04, 0x08)
    private val colMasks = intArrayOf(0x10, 0x20, 0x40, 0x80)
    private var lastKeyPressed: Char = NONE

    fun init() {
        HAL.writeBits(0x0F, 0x0F) // set all row lines to 1
    }

    fun getKey(): Char {
        for (row in 0..3) {
            HAL.clearBits(rowMasks[row])
            for (col in 0..3) {
                if (!HAL.isBit(colMasks[col])) {
                    val key = keyMap[row][col]
                    HAL.setBits(rowMasks[row])

                    if (key != lastKeyPressed) {
                        lastKeyPressed = key
                        return key
                    } else {
                        return NONE
                    }
                }
            }
            HAL.setBits(rowMasks[row])
        }

        // Hiçbir tuşa basılmamışsa lastKeyPressed sıfırla
        if (lastKeyPressed != NONE) {
            lastKeyPressed = NONE
        }

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
}
