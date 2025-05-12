object KBD {
    private val KEY_MAP = mapOf(
        0x0001 to '1', 0x0002 to '2', 0x0004 to '3', 0x0008 to 'A',
        0x0010 to '4', 0x0020 to '5', 0x0040 to '6', 0x0080 to 'B',
        0x0100 to '7', 0x0200 to '8', 0x0400 to '9', 0x0800 to 'C',
        0x1000 to '*', 0x2000 to '0', 0x4000 to '#', 0x8000 to 'D'
    )
    private const val KEY_MASK = 0xFFFF

    fun init() = println("Keyboard initialized.")

    fun getKey(): Char {
        val raw = HAL.readBits(KEY_MASK)
        return KEY_MAP.entries.firstOrNull { raw and it.key != 0 }?.value ?: ' '
    }

    fun waitKey(timeout: Long): Char {
        val start = System.currentTimeMillis()
        while (System.currentTimeMillis() - start < timeout) {
            val k = getKey()
            if (k != ' ') return k
        }
        return ' '
    }
}
