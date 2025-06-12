class MaintenanceMenu(
    private val stats: StatisticsManager,
    private val fileManager: FileManager
) {
    fun run() {
        while (true) {
            LCD.clear()
            LCD.cursor(0, 0)
            LCD.writeString("Maint. Mode")
            LCD.cursor(1, 0)
            LCD.writeString("1:Show 2:Reset 0:Exit")

            while (!KBD.dataAvailable()) Thread.sleep(10)
            val opt = KBD.getKey()
            when (opt) {
                '1' -> {
                    LCD.clear()
                    LCD.cursor(0, 0)
                    LCD.writeString("Games:${stats.gamesPlayed}")
                    LCD.cursor(1, 0)
                    LCD.writeString("Wins:${stats.totalWins}")
                    Thread.sleep(1500)

                    val numbersStr = stats.drawnNumbers.entries.joinToString(" ") { "${it.key}:${it.value}" }
                    LCD.clear()
                    LCD.cursor(0, 0)
                    LCD.writeString("Drawn:")
                    LCD.cursor(1, 0)
                    LCD.writeString(numbersStr.take(16))
                    Thread.sleep(2000)
                }
                '2' -> {
                    stats.reset()
                    fileManager.save(stats)
                    LCD.clear()
                    LCD.cursor(0, 0)
                    LCD.writeString("Stats Reset!")
                    Thread.sleep(1200)
                }
                '0' -> {
                    LCD.clear()
                    LCD.cursor(0, 0)
                    LCD.writeString("Exit Maint.")
                    Thread.sleep(500)
                    return
                }
                else -> {
                    LCD.clear()
                    LCD.cursor(0, 0)
                    LCD.writeString("Invalid Option")
                    Thread.sleep(700)
                }
            }
        }
    }
}
