
object RouletteDisplay {


    fun setValue(cmd: Int, data: Int) {
        val payload = ((data and 0b11111) shl 3) or (cmd and 0b111)
        SerialEmitter.send(SerialEmitter.Destination.ROULETTE, payload, 8)
    }


    fun sendUpdateDisplay() {
        setValue(6, 0)
    }

    fun setDisplayOn(isOn: Boolean) {
        setValue(7, if (isOn) 0 else 1)
    }

    fun clearAll() {
        for (disp in 0..5) {
            setValue(disp, 0)
        }
    }

    fun init() {
        setDisplayOn(true)
        clearAll()
        sendUpdateDisplay()
    }

    fun animation() {
        repeat(3) {
            for (code in 17..22) {
                for (display in 0..5) {
                    setValue(display, code)
                }
                sendUpdateDisplay()
            }
        }

        sendUpdateDisplay()
    }

    fun off() {
        setDisplayOn(false)
        clearAll()
        sendUpdateDisplay()
    }
}




fun main() {
    HAL.init()
    SerialEmitter.init()

    RouletteDisplay.init()

    RouletteDisplay.setValue(1,5)
    RouletteDisplay.sendUpdateDisplay()
    RouletteDisplay.animation()

}
