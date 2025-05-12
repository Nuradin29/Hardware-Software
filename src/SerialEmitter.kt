object SerialEmitter {
    enum class Destination { LCD, ROULETTE }

    fun init() = println("Serial Emitter initialized.")

    fun send(addr: Destination, data: Int, size: Int) {
        println("\nSending to $addr: data=$data, size=$size")
        when (addr) {
            Destination.LCD -> {
                val txt = data.toString().padStart(size, ' ').take(16)
                LCD.write(txt)
                println("LCD displays: \"$txt\"")
            }
            Destination.ROULETTE -> {
                // her hane 0..9 arası, 6 haneyi aşmasın
                val v = data.coerceIn(0, 999999)
                RouletteDisplay.setValue(v)
                println("Roulette Display shows: $v")
            }
        }
    }
}
