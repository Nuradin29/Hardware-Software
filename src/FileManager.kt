import java.io.File

class FileManager(
    private val statsFile: String = "stats.txt",
    private val numbersFile: String = "drawn_numbers.txt"
) {
    // Kaydet
    fun save(stats: StatisticsManager) {
        // Oyun ve kazançlar
        File(statsFile).writeText("${stats.gamesPlayed};${stats.totalWins}")
        // Çıkan sayılar
        File(numbersFile).printWriter().use { out ->
            stats.drawnNumbers.forEach { (num, count) ->
                out.println("$num;$count")
            }
        }
    }

    // Yükle
    fun load(stats: StatisticsManager) {
        // Oyun ve kazançlar
        val statsF = File(statsFile)
        if (statsF.exists()) {
            val parts = statsF.readText().split(";")
            if (parts.size == 2) {
                stats.gamesPlayed = parts[0].toIntOrNull() ?: 0
                stats.totalWins = parts[1].toIntOrNull() ?: 0
            }
        }
        // Çıkan sayılar
        val numbersF = File(numbersFile)
        stats.drawnNumbers.clear()
        if (numbersF.exists()) {
            numbersF.forEachLine { line ->
                val p = line.split(";")
                if (p.size == 2) stats.drawnNumbers[p[0][0]] = p[1].toIntOrNull() ?: 0
            }
        }
    }
}
