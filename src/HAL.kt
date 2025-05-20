import isel.leic.UsbPort

object HAL {
    private var initialized = false
    var portValue = 0  // Yazılan değeri hafızada tut

    fun init() {
        if (!initialized) {
            UsbPort.write(0)
            portValue = 0
            initialized = true
        }
    }

    fun isBit(mask: Int): Boolean {
        init()
        return (UsbPort.read() and mask) != 0
    }

    fun readBits(mask: Int): Int {
        init()
        return UsbPort.read() and mask
    }

    fun setBits(mask: Int) {
        init()
        portValue = portValue or mask
        UsbPort.write(portValue)

    }

    fun clrBits(mask: Int) {
        init()
        portValue = portValue and mask.inv()
        UsbPort.write(portValue)

    }



}
