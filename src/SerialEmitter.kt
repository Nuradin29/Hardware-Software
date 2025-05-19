object SerialEmitter {
    enum class Destination { LCD, ROULETTE }

    private fun getSSMask(dest: Destination): Int =
        when (dest) {
            Destination.LCD -> Masks.O0        // LCD için Chip Select pini
            Destination.ROULETTE -> Masks.O1   // Rulet için Chip Select pini
        }

    fun init() {
        // CS, Clock ve Data pinlerini başlangıçta sıfırla
        HAL.clrBits(Masks.O0 or Masks.O1 or Masks.O3 or Masks.O4)
    }
    fun send(addr: Destination, data: Int, size: Int) {
        val ss = getSSMask(addr)
        println("CS set")
        HAL.setBits(ss)  // CS aktif
        for (i in size - 1 downTo 0) {
            val bit = (data shr i) and 1
            println("Sending bit $i = $bit")
            if (bit == 1) HAL.setBits(Masks.O3) else HAL.clrBits(Masks.O3)
            HAL.setBits(Masks.O4)
            Thread.sleep(5)
            HAL.clrBits(Masks.O4)
            Thread.sleep(5)
        }

        HAL.clrBits(ss)  // CS pasif
        println("CS cleared")
    }

    fun pulseEnable() {
        HAL.setBits(Masks.O7)   // Enable HIGH
        Thread.sleep(10)
        HAL.clrBits(Masks.O7)   // Enable LOW
        Thread.sleep(10)
    }


}



