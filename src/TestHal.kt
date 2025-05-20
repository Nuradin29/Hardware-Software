import isel.leic.UsbPort

fun testHAL() {
    println("HAL testi başlıyor...")

    HAL.init()

    while (true) {
        HAL.setBits(Masks.O0)
        Thread.sleep(1000)

        HAL.clrBits(Masks.O0)
        Thread.sleep(1000)

        HAL.setBits(Masks.O1)
        Thread.sleep(1000)

        HAL.clrBits(Masks.O1)
        Thread.sleep(1000)
    }
}
fun testIsBitReadBits() {
    HAL.init()
    while (true) {
        val isI5High = HAL.isBit(Masks.I5)
        println("I5 pini durumu: ${if (isI5High) "YÜKSEK" else "ALÇAK"}")

        val inputBits = HAL.readBits(
            Masks.I0 or Masks.I1 or Masks.I2 or Masks.I3 or
                    Masks.I4 or Masks.I5 or Masks.I6 or Masks.I7
        )
        println("Tüm giriş pinlerinin durumu (I0..I7): ${inputBits.toString(2).padStart(8, '0')}")
        Thread.sleep(1000)
    }
}


fun main() {
    testHAL()
    testIsBitReadBits()
}
