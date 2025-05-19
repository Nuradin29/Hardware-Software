fun main() {
    HAL.init()
    SerialEmitter.init()
    KBD.init()
    LCD.clear()
    RouletteGame.start()
}
