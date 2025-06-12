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
        RouletteDisplay.init()

        fileManager.load(stats)

        LCD.clear()
        LCD.cursor(0, 0)
        LCD.writeString("Roulette Game")
        LCD.cursor(1, 0)
        LCD.writeString("1 2 3  $0")

        var inBettingMode = false

        while (true) {
            // Coin kontrolü
            if (CoinAcceptor.isCoinInserted()) {
                if (CoinAcceptor.acceptCoin()) {
                    coinDeposit.deposit()
                    if (!inBettingMode) {
                        LCD.cursor(1, 0)
                        LCD.writeString("1 2 3  $${coinDeposit.balance}   ")
                    }
                    Thread.sleep(50)
                } else {
                    LCD.cursor(1, 0)
                    LCD.writeString("Invalid coin      ")
                    Thread.sleep(50)
                    if (!inBettingMode) {
                        LCD.cursor(1, 0)
                        LCD.writeString("1 2 3  $${coinDeposit.balance}   ")
                    }
                    HAL.setBits(Masks.I6)
                    HAL.clrBits(Masks.I6)
                }
                CoinAcceptor.stopAccepting()
            }

            // Klavye kontrolü
            if (KBD.dataAvailable()) {
                val key = KBD.getKey()

                // Bakım menüsüne giriş
                if (key == '0') {
                    maintenanceMenu.run()
                    // Bakım modundan çıkınca oyun ekranına geri dön
                    LCD.clear()
                    LCD.cursor(0, 0)
                    LCD.writeString("Roulette Game")
                    LCD.cursor(1, 0)
                    LCD.writeString("1 2 3  $${coinDeposit.balance}   ")
                    continue
                }

                when (key) {
                    '*' -> {
                        if (!inBettingMode && coinDeposit.balance == 0) {
                            LCD.clear()
                            LCD.cursor(0, 0)
                            LCD.writeString("Insert coin")
                            Thread.sleep(1000)
                            LCD.clear()
                            LCD.cursor(0, 0)
                            LCD.writeString("Roulette Game")
                            LCD.cursor(1, 0)
                            LCD.writeString("1 2 3  $${coinDeposit.balance}   ")
                        } else if (!inBettingMode) {
                            inBettingMode = true
                            LCD.clear()
                            LCD.cursor(1, 0)
                            LCD.writeString("0123456789ABCD")
                        }
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

                            // İSTATİSTİK GÜNCELLEME
                            stats.recordGame(resultChar, win)
                            fileManager.save(stats)

                            Thread.sleep(500)
                            bets.clear()

                            LCD.clear()
                            LCD.cursor(0, 0)
                            LCD.writeString("Roulette Game")
                            LCD.cursor(1, 0)
                            LCD.writeString("Credit: $${coinDeposit.balance} ")
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
