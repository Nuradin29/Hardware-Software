import isel.leic.UsbPort


fun readBits(mask : Int): Int {
    return UsbPort.read() and mask
}

fun isBit(mask: Int): Boolean {
    return (UsbPort.read() and mask) != 0
}
fun setBits(mask: Int) {
    val value = UsbPort.read()
    UsbPort.write(mask or value ) /*write the variable so bit doesnt get overwritten */
}
fun clrBits(mask: Int) {

    val value = UsbPort.read()
    UsbPort.write((1 xor mask )or value )
}
fun writeBits(mask: Int, value: Int): Int {
    return (mask or value)
}


fun main() {
    UsbPort.write(0x00)
    println(readBits(0x0F))
    println(readBits(0xF0))
    println(isBit(0x08))
    println(isBit(0x80))
    setBits(0x08)
    println(isBit(0x08))
    clrBits(0x03)
    writeBits(0x03,0x09)
}


