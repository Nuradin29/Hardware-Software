object RouletteGame {
    private val coinDeposit = CoinDeposit()
    private val bets = mutableMapOf<Char, Int>()
    private val bettableKeys = "0123456789ABCD"
    private val stats = StatisticsManager()
    private val fileManager = FileManager()
    private val maintenanceMenu = MaintenanceMenu(stats, fileManager)

    fun start() {
        HAL.init()
        KBD.init()
        SerialEmitter.init()
        LCD.init()
        LCD.clear()
        LCD.cursor(0, 0)
        LCD.writeString("Roulette Game")
        LCD.cursor(1, 0)
        LCD.writeString("1 2 3  $${coinDeposit.balance}")
        RouletteDisplay.init()
        RouletteDisplay.animation()
        fileManager.load(stats)

        fun resetToStartScreen() {
            LCD.clear()
            LCD.cursor(0, 0)
            LCD.writeString("Roulette Game")
            LCD.cursor(1, 0)
            LCD.writeString("1 2 3  $${coinDeposit.balance}")
            // Display 0: bakiye, diğerleri 0
            RouletteDisplay.setValue(0, coinDeposit.balance.coerceIn(0, 31))
            for (disp in 1..5) RouletteDisplay.setValue(disp, 0)
            RouletteDisplay.sendUpdateDisplay()

        }



        var inBettingMode = false
        bets.clear()

        while (true) {
            // Coin kontrolü
            if (CoinAcceptor.isCoinInserted()) {
                if (CoinAcceptor.acceptCoin()) {
                    coinDeposit.deposit()
                    if (!inBettingMode) {
                        resetToStartScreen()
                    }
                    Thread.sleep(50)
                } else {
                    LCD.cursor(1, 0)
                    LCD.writeString("Invalid coin      ")
                    Thread.sleep(50)
                    if (!inBettingMode) {
                        resetToStartScreen()
                    }
                    HAL.setBits(Masks.I6)
                    HAL.clrBits(Masks.I6)
                }
                CoinAcceptor.stopAccepting()
            }

            // Klavye kontrolü
            if (KBD.dataAvailable()) {
                val key = KBD.getKey()

                if (key == '0') {
                    maintenanceMenu.run()
                    resetToStartScreen()
                    continue
                }

                when (key) {
                    '*' -> {
                        inBettingMode = true
                        bets.clear()
                        // Display 0’da bakiye, diğerleri 0
                        RouletteDisplay.setValue(0, coinDeposit.balance.coerceIn(0, 31))
                        for (disp in 1..5) RouletteDisplay.setValue(disp, 0)
                        RouletteDisplay.sendUpdateDisplay()
                        LCD.clear()
                        LCD.cursor(1, 0)
                        LCD.writeString("0123456789ABCD")
                    }
                    in bettableKeys -> {
                        if (inBettingMode) {
                            val totalBet = bets.values.sum()
                            if (totalBet < coinDeposit.balance) {
                                val current = bets.getOrDefault(key, 0)
                                if (current < 9) {
                                    bets[key] = current + 1
                                    val pos = bettableKeys.indexOf(key)
                                    LCD.cursor(0, pos)
                                    LCD.write('0' + bets[key]!!)
                                    // KALAN PARA display 0'da, diğerleri 0
                                    val kalanPara = coinDeposit.balance - bets.values.sum()
                                    RouletteDisplay.setValue(0, kalanPara.coerceIn(0, 31))
                                    for (disp in 1..5) RouletteDisplay.setValue(disp, 0)
                                    RouletteDisplay.sendUpdateDisplay()
                                }
                            }
                        }
                    }
                    '#' -> {
                        if (inBettingMode && bets.isNotEmpty()) {
                            inBettingMode = false
                            LCD.clear()
                            LCD.cursor(0, 0)
                            LCD.writeString("Spinning...")
                            RouletteDisplay.animation()

                            val resultIndex = (0 until bettableKeys.length).random()
                            val resultChar = bettableKeys[resultIndex]

                            LCD.cursor(1, 0)
                            LCD.writeString("Result: $resultChar ")

                            // Sonucu tüm displaylere yaz
                            for (i in 0..5) {
                                RouletteDisplay.setValue(i, resultIndex)
                            }
                            RouletteDisplay.sendUpdateDisplay()

                            val totalBet = bets.values.sum()
                            repeat(totalBet) { coinDeposit.useCredit(1) }

                            val betAmount = bets[resultChar] ?: 0
                            val win = betAmount > 0
                            if (win) {
                                val won = betAmount * 5
                                coinDeposit.balance += won
                                LCD.cursor(0, 0)
                                LCD.writeString("You win! +$$won  ")
                            } else {
                                LCD.cursor(0, 0)
                                LCD.writeString("No win!          ")
                            }

                            stats.recordGame(resultChar, win)
                            fileManager.save(stats)

                            Thread.sleep(1000)
                            // Sonrasında: oyun başa dönsün ama ANİMASYON YOK!
                            resetToStartScreen()
                        }
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
