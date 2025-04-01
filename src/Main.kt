import isel.leic.UsbPort

/*fun main(args : Array<String>)  {
    while (true) {
        val value = UsbPort.read()
        UsbPort.write(value)
    }
}
 */
fun main() {
    while (true) {
        val value = UsbPort.read() and 0b00001111
        UsbPort.write(value)
    }
}
