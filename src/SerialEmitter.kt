object SerialEmitter {
    enum class Destination { LCD, ROULETTE }

    private fun getSSMask(dest: Destination): Int =
        when (dest) {
            Destination.LCD -> Masks.O0
            Destination.ROULETTE -> Masks.O1
        }
    fun init() {
        HAL.clrBits(Masks.O0 or Masks.O1 or Masks.O3 or Masks.O4)
        println("[SerialEmitter] Initialized")
    }


    fun send(addr: Destination, data: Int, size: Int) {
        val ss = getSSMask(addr)
        println("[SerialEmitter] CS set for $addr")
        HAL.setBits(ss)
        for (i in size - 1 downTo 0) {
            val bit = (data shr i) and 1
            println("[SerialEmitter] Sending bit $i = $bit")
            if (bit == 1) HAL.setBits(Masks.O3) else HAL.clrBits(Masks.O3)
            HAL.setBits(Masks.O4)
            Thread.sleep(5)
            HAL.clrBits(Masks.O4)
            Thread.sleep(5)
        }
        HAL.clrBits(ss)
        println("[SerialEmitter] CS cleared for $addr")
    }

    fun pulseEnable() {
        HAL.setBits(Masks.O7)
        Thread.sleep(10)
        HAL.clrBits(Masks.O7)
        Thread.sleep(10)
    }
}
