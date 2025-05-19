object RouletteDisplay {
    fun init() {
        setValue(0)
        off(false)
    }

    fun animation() {
        for (i in 1..20) {
            setValue((1..36).random())
            Thread.sleep(50)
        }
    }

    fun setValue(value: Int) {
        SerialEmitter.send(SerialEmitter.Destination.ROULETTE, value, 8)
    }

    fun off(value: Boolean) {
        if (value) {
            SerialEmitter.send(SerialEmitter.Destination.ROULETTE, 0, 8)
        }
    }
}
