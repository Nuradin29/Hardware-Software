import isel.leic.UsbPort

object HAL {
    fun init() {
        // Başlatma işlemleri gerekiyorsa buraya
    }

    fun readBits(mask : Int): Int {
        return UsbPort.read() and mask
    }

    fun isBit(mask: Int): Boolean {
        return (UsbPort.read() and mask) != 0
    }
    fun setBits(mask: Int) {
        val value = UsbPort.read()
        UsbPort.write(value or mask) /*write the variable so bit doesnt get overwritten */
    }
    fun clearBits(mask: Int) {
        val value = UsbPort.read()
        UsbPort.write(value and mask.inv())
    }
    fun writeBits(mask: Int, value: Int) {
        val current = UsbPort.read()
        val cleared = current and mask.inv() // mask bits = 0
        val updated = cleared or (value and mask)
        UsbPort.write(updated)
    }
}

/*fun main() {
    UsbPort.write(0x00)
    println(HAL.readBits((0x0F))
    println(HAL.readBits(0xF0))
    println(HAL.isBit(0x08))
    println(HAL.isBit(0x80))
    setBits(0x08)
    println(HAL.isBit(0x08))
    clrBits(0x03)
    writeBits(0x03,0x09)
}*/


