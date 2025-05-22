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
            println("\nEnter 'c' to insert coin, '*' to place a bet, '#' to start the game.")
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
                        println("No bets placed yet!")
                    }
                }
                else -> println("Invalid input!")
            }
        }
    }

    fun insertCoin() {
        credit += 2
        println("Coin inserted. Current credit: $$credit")
    }

    fun updateCreditDisplay() {
        println("Total credit: $$credit")
    }

    fun handleBet(scanner: Scanner) {
        print("Enter the number you want to bet on (0–13): ")
        val number = scanner.nextLine().toIntOrNull()
        if (number == null || number !in 0..13) {
            println("Invalid number!")
            return
        }

        print("How much do you want to bet on number $number? (max $maxBetPerNumber): ")
        val amount = scanner.nextLine().toIntOrNull()
        if (amount == null || amount !in 1..maxBetPerNumber || amount > credit) {
            println("Invalid amount or insufficient credit!")
            return
        }

        bets[number] = bets.getOrDefault(number, 0) + amount
        credit -= amount
        println("Placed a $amount dollar bet on $number. Remaining credit: $$credit")
    }

    fun playGame() {
        println("\nSpinning the wheel...")
        Thread.sleep(1000)
        val result = (0..13).random()
        println("Result: $result")

        var totalWin = 0
        bets.forEach { (num, betAmt) ->
            if (num == result) {
                totalWin += betAmt * 5
            }
        }

        credit += totalWin
        updateCreditDisplay()

        if (totalWin > 0) {
            println("Congratulations! You won $$totalWin!")
        } else {
            println("Unfortunately, you lost.")
        }

        bets.clear()
    }
}

fun main() {
    RouletteGameTUI.start()
}
