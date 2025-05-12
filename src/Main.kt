fun main() {
    HAL.init()
    KBD.init()
    LCD.init()
    SerialEmitter.init()
    RouletteDisplay.init()

    // LCD testi
    SerialEmitter.send(SerialEmitter.Destination.LCD, 1234, 4)
    Thread.sleep(500)

    // 7-segment testi
    SerialEmitter.send(SerialEmitter.Destination.ROULETTE, 987654, 6)
    Thread.sleep(500)
    SerialEmitter.send(SerialEmitter.Destination.ROULETTE, 42, 6)
    Thread.sleep(500)
    SerialEmitter.send(SerialEmitter.Destination.ROULETTE, 1234567, 6)
}
