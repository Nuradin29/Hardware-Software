object RouletteDisplay {
    // Başlatıcı fonksiyon: Göstergenin başlangıç durumunu ayarlar
    fun init() {
        setValue(0)      // Göstergede 0'ı göster
        off(false)       // Göstergede görüntüyü aç
    }

    // Rastgele sayı animasyonu (örneğin dönerken hızlıca değişen değerler gibi)
    fun animation() {
        for (i in 1..20) {
            setValue((0..16).random()) // 1-16 arası rastgele bir değer ata
            Thread.sleep(50)           // Biraz bekle
        }
    }


    // Göstergeye yeni değer gönderir
    fun setValue(value: Int) {
        SerialEmitter.send(SerialEmitter.Destination.ROULETTE, value, 8)
    }

    // Göstergeyi kapatır/açar
    fun off(value: Boolean) {
        if (value) {
            SerialEmitter.send(SerialEmitter.Destination.ROULETTE, 0, 8)
        }
    }

}
fun main() {
    HAL.init()
    SerialEmitter.init()
    LCD.init()
    LCD.cursor(0, 0)
    LCD.writeString("Roulette Game")
    LCD.cursor(1, 0)
    LCD.writeString("1 2 3 $0")
    RouletteDisplay.init()      // Başlangıç: 0 göster ve aç

    println("Gösterge test başlıyor...")

    for (i in 1..10) {
        val valGoster = (0..31).random()  // 5-bit değer gönder
        println("Gösterilen: $valGoster")
        RouletteDisplay.setValue(valGoster)
        Thread.sleep(500)  // Gözle görülür değişim için bekle
    }

    println("Göstergeden çıkılıyor, kapatılıyor...")
    RouletteDisplay.off(true)   // Göstergeyi kapat (veriyi 0 gönder)
}
