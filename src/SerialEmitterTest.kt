fun main() {
    // Test için gerekli modülleri başlat
    HAL.init()
    SerialEmitter.init()

    // LCD'ye test verisi gönder (0xA - binary: 1010)
    println("\n🔹 LCD Testi Başlıyor 🔹")
    SerialEmitter.send(SerialEmitter.Destination.LCD, 0xA, 4)

    // Roulette'e test verisi gönder (0x1C - binary: 11100)
    println("\n🔸 Roulette Testi Başlıyor 🔸")
    SerialEmitter.send(SerialEmitter.Destination.ROULETTE, 0x1C, 5)
}
