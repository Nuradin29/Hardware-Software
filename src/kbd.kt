object KBD {
    const val NONE = '\u0000' // Special value indicating no key was pressed
    private val validKeys = "0123456789ABCD#*" // Simulated valid key inputs

    // Initializes the simulated keyboard
    fun init() {
        println("Keyboard initialized.")
    }

    // Simulate getting a keypress from the keyboard (randomly)
    fun getKey(): Char {
        return if ((0..10).random() > 7) validKeys.random() else NONE
    }

    // Wait for a keypress for a specified timeout in milliseconds
    fun waitKey(timeout: Long): Char {
        val startTime = System.currentTimeMillis()
        while (System.currentTimeMillis() - startTime < timeout) {
            val key = getKey()
            if (key != NONE) return key
        }
        return NONE
    }
}

fun main() {

    // Initialize the simulated keyboard
    KBD.init()

    // Test immediate key retrieval (simulated key press)
    val key1 = KBD.getKey()
    println("Key pressed immediately: ${if (key1 != KBD.NONE) key1 else "NONE"}")

    // Test waiting for a key with a timeout (3 seconds simulation)
    val key2 = KBD.waitKey(3000) // Wait for up to 3 seconds
    println("Waited key (within timeout): ${if (key2 != KBD.NONE) key2 else "NONE"}")

    // Test waiting for a key with a timeout (no key pressed within timeout)
    val key3 = KBD.waitKey(3000) // Another 3 seconds wait with no key press
    println("Waited key (after timeout): ${if (key3 != KBD.NONE) key3 else "NONE"}")
}
