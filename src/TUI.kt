import java.util.Scanner

object RouletteGameTUI {
    var credit = 0
    val maxBetPerNumber = 9
    val bets = mutableMapOf<Int, Int>()

    fun start() {
        val scanner = Scanner(System.`in`)
        println("ROULETTE GAME")
        updateCreditDisplay()

        while (true) {
            println("\nJeton eklemek için 'c', bahis yapmak için '*', oyunu başlatmak için '#' girin.")
            val input = scanner.nextLine()

            when (input) {
                "c" -> {
                    insertCoin()
                }
                "*" -> {
                    handleBet(scanner)
                }
                "#" -> {
                    if (bets.isNotEmpty()) {
                        playGame()
                    } else {
                        println("Henüz bahis yapılmadı!")
                    }
                }
                else -> println("Geçersiz giriş!")
            }
        }
    }

    fun insertCoin() {
        credit += 2
        println("Jeton eklendi. Mevcut kredi: $$credit")
    }

    fun updateCreditDisplay() {
        println("Toplam kredi: $$credit")
    }

    fun handleBet(scanner: Scanner) {
        print("Bahis yapılacak sayıyı girin (1-16): ")
        val number = scanner.nextLine().toIntOrNull()
        if (number == null || number !in 0..16) {
            println("Geçersiz sayı!")
            return
        }

        print("$number sayısına kaç dolar bahis koyacaksınız (max 9): ")
        val amount = scanner.nextLine().toIntOrNull()
        if (amount == null || amount !in 1..maxBetPerNumber || amount > credit) {
            println("Geçersiz miktar veya yetersiz kredi!")
            return
        }

        bets[number] = bets.getOrDefault(number, 0) + amount
        credit -= amount
        println("$number sayısına $amount dolar bahis yapıldı. Kalan kredi: $$credit")
    }

    fun playGame() {
        println("\nÇark dönüyor...")
        Thread.sleep(1000)
        val result = (0..16).random()
        println("Sonuç: $result")

        var totalWin = 0
        bets.forEach { (num, betAmt) ->
            if (num == result) {
                totalWin += betAmt * 5
            }
        }

        credit += totalWin
        updateCreditDisplay()

        if (totalWin > 0) {
            println("Tebrikler, $totalWin dolar kazandınız!")
        } else {
            println("Maalesef, kaybettiniz.")
        }

        bets.clear()
    }
}

fun main() {
    RouletteGameTUI.start()
}
