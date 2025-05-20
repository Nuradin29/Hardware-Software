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

        // CS LOW (başlat)
        HAL.clrBits(dest)
        HAL.clrBits(Masks.O4)

        var oneCount = 0

        for (i in 0 until size) {
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

        // ⬅️ ODD parity hesaplama (toplam 1 sayısı tek olmalı)
        val parityBit = if (oneCount % 2 == 0) 1 else 0

        if (parityBit == 1) HAL.setBits(Masks.O3) else HAL.clrBits(Masks.O3)
        HAL.setBits(Masks.O4)
        Thread.sleep(1)
        HAL.clrBits(Masks.O4)
        Thread.sleep(1)

        HAL.setBits(dest) // CS HIGH (bitir)
    }
}
