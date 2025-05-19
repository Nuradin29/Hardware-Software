fun main() {
    HAL.init()
    KBD.init()


    while (true) {
        // Klavye dinle
        if (KBD.dataAvailable()) {
            val key = KBD.getKey()
            println("Tuş: $key")
            KBD.ack()
        }
        // Jeton kontrolü
        if (Acceptor.isCoinInserted()) {
            println("Jeton algılandı!")
            Acceptor.acceptCoin()
            Thread.sleep(100)
            Acceptor.stopAccepting()
        }
        Thread.sleep(10)
    }
}