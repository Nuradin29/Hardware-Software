class StatisticsManager {
    var gamesPlayed = 0
    var totalWins = 0
    val drawnNumbers = mutableMapOf<Char, Int>() // Hangi sayı kaç kere çıktı

    fun recordGame(result: Char, win: Boolean) {
        gamesPlayed++
        drawnNumbers[result] = (drawnNumbers[result] ?: 0) + 1
        if (win) totalWins++
    }

    fun reset() {
        gamesPlayed = 0
        totalWins = 0
        drawnNumbers.clear()
    }

    // Görüntüleme (Bakım menüsünde kullanılabilir)
    fun printStats() {
        println("Total Games Played: $gamesPlayed")
        println("Total Wins: $totalWins")
        println("Drawn Numbers:")
        drawnNumbers.forEach { (num, count) ->
            println("  $num: $count")
        }
    }
}
