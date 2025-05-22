object RouletteGame {
    private var credit = 0
    private val bets = mutableMapOf<Char, Int>()
    private val bettableKeys = "0123456789ABCD"

    fun start() {

        HAL.init()
        KBD.init()
        SerialEmitter.init()
        LCD.init()
        RouletteDisplay.init()

        LCD.clear()
        LCD.cursor(0, 0)
        LCD.writeString("Roulette Game")
        LCD.cursor(1, 0)
        LCD.writeString("1 2 3  $0")

        var inBettingMode = false

        while (true) {
            // Coin kontrolü
            if (Acceptor.isCoinInserted()) {
                if (Acceptor.acceptCoin()) {
                    credit += 2
                    if (!inBettingMode) {
                        LCD.cursor(1, 0)
                        LCD.writeString("1 2 3  $$credit   ")
                    }
                    Thread.sleep(50) // ← spam engelleme burada
                } else {
                    LCD.cursor(1, 0)
                    LCD.writeString("Invalid coin      ")
                    Thread.sleep(50)
                    if (!inBettingMode) {
                        LCD.cursor(1, 0)
                        LCD.writeString("1 2 3  $$credit   ")
                    }
                    HAL.setBits(Masks.I6)
                    HAL.clrBits(Masks.I6)
                }
                Acceptor.stopAccepting()
            }

            // Klavye kontrolü
            if (KBD.dataAvailable()) {
                val key = KBD.getKey()

                when (key) {
                    '*' -> {
                        if (inBettingMode) {
                            // Zaten bahis modundaysa yıldız tuşu etkisiz
                            // İstersen kullanıcıya bir uyarı da gösterebiliriz
                        } else if (credit == 0) {
                            LCD.clear()
                            LCD.cursor(0, 0)
                            LCD.writeString("Insert coin")
                            Thread.sleep(1000)
                            LCD.clear()
                            LCD.cursor(0, 0)
                            LCD.writeString("Roulette Game")
                            LCD.cursor(1, 0)
                            LCD.writeString("1 2 3  $$credit   ")
                        } else {
                            inBettingMode = true
                            LCD.clear()
                            LCD.cursor(1, 0)
                            LCD.writeString("0123456789ABCD")
                        }
                    }


                    in bettableKeys -> {
                        if (inBettingMode) {
                            val totalBet = bets.values.sum()
                            if (totalBet < credit) {
                                val current = bets.getOrDefault(key, 0)
                                if (current < 9) {
                                    bets[key] = current + 1
                                    val pos = bettableKeys.indexOf(key)
                                    LCD.cursor(0, pos)
                                    LCD.write('0' + bets[key]!!)
                                }
                            }
                        }
                    }

                    '#' -> {
                        if (inBettingMode) {
                            if (bets.isEmpty()) continue

                            inBettingMode = false
                            LCD.clear()
                            LCD.cursor(0, 0)
                            LCD.writeString("Spinning...")
                            RouletteDisplay.animation()

                            val resultIndex = (0 until bettableKeys.length).random()
                            val resultChar = bettableKeys[resultIndex]

                            LCD.cursor(1, 0)
                            LCD.writeString("Result: $resultChar ")

                            RouletteDisplay.showResult(resultIndex)

                            val totalBet = bets.values.sum()
                            credit -= totalBet
                            if (credit < 0) credit = 0

                            val betAmount = bets[resultChar] ?: 0
                            if (betAmount > 0) {
                                val won = betAmount * 5
                                credit += won
                                LCD.cursor(0, 0)
                                LCD.writeString("You win! +$$won  ")
                            } else {
                                LCD.cursor(0, 0)
                                LCD.writeString("No win!          ")
                            }

                            Thread.sleep(500)
                            bets.clear()

                            LCD.clear()
                            LCD.cursor(0, 0)
                            LCD.writeString("Roulette Game")
                            LCD.cursor(1, 0)
                            LCD.writeString("Credit: $$credit ")
                        }
                    }


                }

                KBD.ack()
            }
            Thread.sleep(10)
        }
        start()
    }

}

fun main() {
    RouletteGame.start()
}
