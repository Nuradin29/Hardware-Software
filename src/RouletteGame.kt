import javax.swing.*
import java.awt.*
import kotlin.random.Random

class RouletteGameGUI : JFrame("Roulette Game") {
    private val lcdDisplay = JTextArea(2, 16)
    private val displayPanel = JPanel(GridLayout(1, 6))
    private val keypadPanel = JPanel(GridLayout(4, 4))
    private val coinButton = JButton("Coin")
    private val sevenSegmentLabels = mutableListOf<JLabel>()

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        layout = BorderLayout()
        size = Dimension(600, 400)
        isResizable = false

        lcdDisplay.isEditable = false
        lcdDisplay.font = Font("Monospaced", Font.BOLD, 18)
        lcdDisplay.text = "Roulette Game\n1  2  3  4  #0"
        add(JScrollPane(lcdDisplay), BorderLayout.NORTH)

        val keys = listOf("1", "2", "3", "A", "4", "5", "6", "B", "7", "8", "9", "C", "*", "0", "#", "D")
        for (key in keys) {
            val button = JButton(key)
            button.addActionListener { onKeyPress(key) }
            keypadPanel.add(button)
        }
        add(keypadPanel, BorderLayout.WEST)

        coinButton.addActionListener { startGame() }
        add(coinButton, BorderLayout.SOUTH)

        for (i in 0..5) {
            val segment = JLabel("0", SwingConstants.CENTER)
            segment.font = Font("Monospaced", Font.BOLD, 40)
            displayPanel.add(segment)
            sevenSegmentLabels.add(segment)
        }
        add(displayPanel, BorderLayout.CENTER)

        HAL.init()
        KBD.init()
        LCD.init()
        SerialEmitter.init()
        RouletteDisplay.init()
    }

    private fun onKeyPress(key: String) {
        lcdDisplay.append("\nKey Pressed: $key")
    }

    private fun startGame() {
        lcdDisplay.text = "Roulette Game\nRolling..."
        Thread {
            Thread.sleep(1000)
            val result = Random.nextInt(0, 36)
            updateDisplay(result)
        }.start()
    }

    private fun updateDisplay(result: Int) {
        lcdDisplay.text = "Roulette Game\nResult: $result"
        RouletteDisplay.setValue(result)
    }
}

fun main() {
    SwingUtilities.invokeLater {
        RouletteGameGUI().apply {
            isVisible = true
        }
    }
}
