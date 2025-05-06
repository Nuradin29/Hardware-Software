fun main() {
    // Tüm modülleri başlat
    HAL.init()
    KBD.init()
    LCD.init()
    SerialEmitter.init()
    RouletteDisplay.init()

    println("Tuş dinleniyor...")

    while (true) {
        // Tuşu al
        val key = KBD.getKey()

        if (key != KBD.NONE) {
            println("Pressed key: $key")
            LCD.clear()  // LCD'yi temizle
            LCD.write("Pressed: $key")  // LCD'ye tuşu yazdır
        }

        // Eğer kullanıcı '1' tuşuna basarsa, roulette döndürülsün
        if (key == '1') {  // '1' tuşu doğrudan karşılaştırılıyor
            println("Rulet dönüyor...")
            RouletteDisplay.rotate()  // Rulet numaralarını döndür

            // Rulet döndüğünde LCD'yi güncelle
            LCD.clear()  // LCD'yi temizle
            LCD.write("Rulet Dönüyor...")  // LCD'ye yazdır
        }

        // CPU'yu yormamak için bekleme
        Thread.sleep(100)
    }
}
