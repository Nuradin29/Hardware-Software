object SerialEmitter {
    enum class Destination {
        LCD, ROULETTE
    }

    fun init() {
        HAL.setBits(Masks.O0 or Masks.O1) // CS HIGH
        HAL.clrBits(Masks.O3 or Masks.O4) // SDX ve SCLK LOW
    }

    fun send(addr: Destination, data: Int, size: Int) {
        val dest = when (addr) {
            Destination.LCD -> Masks.O0
            Destination.ROULETTE -> Masks.O1
        }

        val bitRange = when (addr) {
            Destination.LCD -> 0 until size
            Destination.ROULETTE -> size - 1 downTo 0
        }

        println("[SEND] to $addr | data = 0b${data.toString(2).padStart(8, '0')}")

        // CS LOW
        HAL.clrBits(dest)
        HAL.clrBits(Masks.O4)

        var oneCount = 0
        for (i in bitRange) {
            val bit = (data shr i) and 1
            if (bit == 1) {
                HAL.setBits(Masks.O3)
                oneCount++
            } else {
                HAL.clrBits(Masks.O3)
            }

            HAL.setBits(Masks.O4)
            Thread.sleep(1)
            HAL.clrBits(Masks.O4)
            Thread.sleep(1)
        }

        // Parity
        val parityBit = if (oneCount % 2 == 0) 1 else 0
        if (parityBit == 1) HAL.setBits(Masks.O3) else HAL.clrBits(Masks.O3)
        HAL.setBits(Masks.O4)
        Thread.sleep(1)
        HAL.clrBits(Masks.O4)
        Thread.sleep(1)

        // CS HIGH
        HAL.setBits(dest)
    }


    fun pulseEnable() {
        HAL.setBits(Masks.O7)
        Thread.sleep(1)
        HAL.clrBits(Masks.O7)
        Thread.sleep(1)
    }
}
