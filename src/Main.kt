fun main() {
    HAL.init()
    KBD.init()

    println("Tuş dinleniyor...")

    while (true) {
        val key = KBD.getKey()
        if (key != KBD.NONE) {
            println("pressed key: $key")
        }
        Thread.sleep(100) // CPU'yu yormamak için bekleme
    }
}