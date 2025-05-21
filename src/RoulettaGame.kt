object RouletteGame {
    private var credit = 0

    fun start() {
        HAL.init()
        KBD.init()
        SerialEmitter.init()
        LCD.init()
        RouletteDisplay.init()

        var credit = 0
        var coinPreviouslyInserted = false

        LCD.clear()
        LCD.cursor(0, 0)
        LCD.writeString("Roulette Game")
        LCD.cursor(1, 0)
        LCD.writeString("1 2 3 $0")

        while (true) {
            // Coin kontrolü
            if (Acceptor.isCoinInserted()) {
                if (!coinPreviouslyInserted) {
                    val coinId = Acceptor.coinID()
                    if (coinId == 1) {
                        Acceptor.acceptCoin()
                        credit += 2
                        LCD.cursor(1, 0)
                        LCD.writeString("1 2 3  $$credit   ")
                    } else {
                        LCD.cursor(1, 0)
                        LCD.writeString("Invalid coin      ")
                    }
                    coinPreviouslyInserted = true
                }
            } else {
                if (coinPreviouslyInserted) {
                    Acceptor.stopAccepting()
                }
                coinPreviouslyInserted = false
            }

            // Klavye kontrolü
            if (KBD.dataAvailable()) {
                val key = KBD.getKey()
                println("Tuş: $key")

                if (key == '*') {
                    if (credit > 0) {
                        credit -= 1
                        LCD.clear()
                        LCD.cursor(0, 0)
                        LCD.writeString("Spinning...")

                        RouletteDisplay.animation()
                        val result = (0..13).random()

                        LCD.cursor(1, 0)
                        LCD.writeString("Result: $result ")
                        RouletteDisplay.showResult(result)
                    } else {
                        LCD.cursor(1, 0)
                        LCD.writeString("No credit!       ")
                    }
                }

                KBD.ack()
            }

            Thread.sleep(10)
        }
    }

}
fun main() {
    RouletteGame.start()
}
