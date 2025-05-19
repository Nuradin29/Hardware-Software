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
