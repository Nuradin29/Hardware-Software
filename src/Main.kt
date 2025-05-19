fun main() {
    HAL.init()
    LCD.init()
    RouletteDisplay.init()
    SerialEmitter.init()

    LCD.clear()
    LCD.cursor(0, 0)
    LCD.writeString("Roulette Game")
    LCD.cursor(1, 0)
    LCD.writeString("1 2 3 $0")

    while (true) {
        // Klavye dinle
        if (KBD.dataAvailable()) {
            val key = KBD.getKey()
            println("Tuş: $key")
            KBD.ack()
        }
        // Jeton kontrolü
        if (Acceptor.isCoinInserted()) {
            println("Jeton algılandı!")
            Acceptor.acceptCoin()
            Thread.sleep(100)
            Acceptor.stopAccepting()
        }
        Thread.sleep(10)
    }
}