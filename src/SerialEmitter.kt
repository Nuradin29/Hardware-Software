import kotlin.concurrent.thread

object SerialEmitter {
    enum class Destination { LCD, ROULETTE }

    fun init() {
        HAL.setBits(Masks.O0 or Masks.O1)
        HAL.clrBits(Masks.O3 or Masks.O4)
    }

    fun send(addr: Destination, data: Int, size: Int = 8) {
        val dest = when (addr) {
            Destination.LCD -> Masks.O0
            Destination.ROULETTE -> Masks.O1
        }

        HAL.clrBits(dest)      // CS LOW
        HAL.clrBits(Masks.O4)  // SCLK LOW

        var oneCount = 0
        for (i in 0 until size) {
            val bit = (data shr i) and 1
            if (bit == 1) {
                HAL.setBits(Masks.O3)   // SDX = 1
                oneCount++
            } else {
                HAL.clrBits(Masks.O3)   // SDX = 0
            }
            HAL.setBits(Masks.O4)
            Thread.sleep(2)
            HAL.clrBits(Masks.O4)
            Thread.sleep(2)
        }

        val parity = if (oneCount % 2 == 0) 1 else 0
        if (parity == 1) HAL.setBits(Masks.O3) else HAL.clrBits(Masks.O3)
        HAL.setBits(Masks.O4)
        Thread.sleep(2)
        HAL.clrBits(Masks.O4)
        Thread.sleep(2)

        HAL.setBits(dest) // CS HIGH
    }

}
fun main() {
    HAL.init()
    SerialEmitter.init()
    val data = 0b11111
    val cmd = 0b000
    val payload = ((data and 0b11111) shl 3) or (cmd and 0b111)

    SerialEmitter.send(SerialEmitter.Destination.ROULETTE, payload, 8)

    println("Payload (data-cmd): 0b${payload.toString(2).padStart(8,'0')} = 0x${payload.toString(16)} gönderildi.")
}

