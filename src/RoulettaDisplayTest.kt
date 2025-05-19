// Mock SerialEmitter - sadece terminale yazacak
object SerialEmitterMock {
    enum class Destination { LCD, ROULETTE }

    fun send(addr: Destination, data: Int, size: Int) {
        if (addr == Destination.ROULETTE) {
            println("🟢 Roulette Display shows: $data")
        }
    }

    fun pulseEnable() { /* Boş bırak, gereksiz */ }
}

// Test için RouletteDisplay'i mock ile yazıyoruz:
object RouletteDisplayMock {
    fun setValue(value: Int) {
        SerialEmitterMock.send(SerialEmitterMock.Destination.ROULETTE, value, 8)
    }

    // Rastgele sayı animasyonu (örneğin dönerken hızlıca değişen değerler gibi)
    fun animation() {
        for (i in 1..20) {
            setValue((0..16).random()) // 1-16 arası rastgele bir değer ata
            Thread.sleep(50)           // Biraz bekle
        }
    }

}

// Ana fonksiyon - test
fun main() {
    RouletteDisplayMock.setValue(0)
    RouletteDisplayMock.setValue(5)
    RouletteDisplayMock.animation()
}
