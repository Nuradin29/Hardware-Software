object RouletteGame {
    var credit = 0               // Toplam para
    val maxBetPerNumber = 9      // Maksimum bir sayıya yatırılabilecek bahis
    val bets = mutableMapOf<Int, Int>()  // Sayıya yatırılan bahisler (sayı -> bahis miktarı)

    fun start() {
        LCD.clear()
        LCD.cursor(0, 0)
        LCD.writeString("ROULETTE GAME")
        updateCreditDisplay()
        RouletteDisplay.init()

        while (true) {
            if (Acceptor.isCoinInserted()) {
                Acceptor.acceptCoin()
                credit += 2   // Jeton 2 dolar varsayalım
                updateCreditDisplay()
                Acceptor.stopAccepting()
            }

            if (KBD.dataAvailable()) {
                val key = KBD.getKey()
                KBD.ack()
                handleKey(key)
            }

            Thread.sleep(50)
        }
    }

    fun updateCreditDisplay() {
        // Sağdaki 7-segment displaylerde toplam krediyi göster
        // Örnek, sadece kredi sayısını göster (daha gelişmiş format eklenebilir)
        SerialEmitter.send(SerialEmitter.Destination.ROULETTE, credit, 8)
    }

    var bettingNumber: Int? = null   // Bahis yapılacak sayı
    var bettingAmount: Int? = null   // Bahis miktarı

    fun handleKey(key: Char) {
        when (key) {
            '*' -> { // Bahis moduna gir
                LCD.clear()
                LCD.cursor(0, 0)
                LCD.writeString("BET MODE: Number?")
                bettingNumber = null
                bettingAmount = null
            }
            '#' -> { // Bahis bitti, oyunu başlat
                if (bets.isNotEmpty()) {
                    playGame()
                } else {
                    LCD.clear()
                    LCD.writeString("No bets placed!")
                }
            }
            in '1'..'9' -> {
                val num = key.digitToInt()
                if (bettingNumber == null) {
                    // Bahis yapılacak sayı seçiliyor (1-9 sınırı)
                    if (num in 1..16) {
                        bettingNumber = num
                        LCD.clear()
                        LCD.writeString("Amount for $num?")
                    } else {
                        LCD.clear()
                        LCD.writeString("Invalid number")
                    }
                } else if (bettingAmount == null) {
                    // Bahis miktarı giriliyor
                    val amount = num
                    if (amount <= maxBetPerNumber && amount <= credit) {
                        bettingAmount = amount
                        placeBet(bettingNumber!!, amount)
                    } else {
                        LCD.clear()
                        LCD.writeString("Max bet 9 or insufficient credit")
                    }
                }
            }
            else -> {
                // Diğer tuşlar veya kullanılmayan tuşlar için
            }
        }
    }

    fun placeBet(number: Int, amount: Int) {
        bets[number] = bets.getOrDefault(number, 0) + amount
        credit -= amount
        updateCreditDisplay()
        LCD.clear()
        LCD.writeString("Bet $amount on $number")
        bettingNumber = null
        bettingAmount = null
    }

    fun playGame() {
        LCD.clear()
        LCD.writeString("Spinning...")
        RouletteDisplay.animation()

        val result = (1..16).random()
        RouletteDisplay.setValue(result)
        LCD.clear()
        LCD.writeString("Result: $result")

        var totalWin = 0
        bets.forEach { (num, betAmt) ->
            if (num == result) {
                totalWin += betAmt * 5
            }
        }
        credit += totalWin
        updateCreditDisplay()

        if (totalWin > 0) {
            LCD.writeString(" Won: $totalWin")
        } else {
            LCD.writeString(" Lost")
        }
        bets.clear()
    }
}
