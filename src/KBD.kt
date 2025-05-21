object KBD {
    const val NONE = '\u0000'
    // Yatay ilerleme
    private val KEYS = arrayOf(
        '1', '2', '3', 'A',
        '4', '5', '6', 'B',
        '7', '8', '9', 'C',
        '*', '0', '#', 'D'
    )
    // Simülatörden gelen indexin KEYS'teki yatay karşılığını verecek tablo
    private val INDEX_MAP = arrayOf(
        0,  4,  8, 12,  // 0: '1', 1: '4', 2: '7', 3: '*'
        1,  5,  9, 13,  // 4: '2', 5: '5', 6: '8', 7: '0'
        2,  6, 10, 14,  // 8: '3', 9: '6',10: '9',11: '#'
        3,  7, 11, 15   //12: 'A',13: 'B',14: 'C',15: 'D'
    )

    fun init() {}

    fun getKey(): Char {
        if (!dataAvailable()) return NONE
        val idx = HAL.readBits(Masks.I0 or Masks.I1 or Masks.I2 or Masks.I3)
        val realIdx = INDEX_MAP[idx]
        ack()
        return if (realIdx in KEYS.indices) KEYS[realIdx] else NONE
    }

    fun waitKey(timeout: Long): Char {
        val start = System.currentTimeMillis()
        while (System.currentTimeMillis() - start < timeout) {
            val key = getKey()
            if (key != NONE) return key
            Thread.sleep(10)
        }
        return NONE
    }

    fun dataAvailable() = HAL.isBit(Masks.I4)
    fun ack() {
        HAL.setBits(Masks.O7)
        HAL.clrBits(Masks.O7)
    }
}
fun main() {
    HAL.init()
    KBD.init()


    var credit = 0
    var coinPreviouslyInserted = false

    while (true) {
        // Klavye tuşlarını dinle
        if (KBD.dataAvailable()) {
            val key = KBD.getKey()
            println("Tuş: $key")
            KBD.ack()
        }

        // Coin kontrolü (eski çalışan kod)
        if (Acceptor.isCoinInserted()) {
            if (!coinPreviouslyInserted) {
                val coinId = Acceptor.coinID()
                if (coinId == 1) {
                    Acceptor.acceptCoin()
                    val creditToAdd = 2
                    credit += creditToAdd
                    println("Kredi eklendi: $creditToAdd, Toplam kredi: $credit")
                } else {
                    println("Coin ID aktif değil, kredi eklenmedi.")

                }
                coinPreviouslyInserted = true
            }
        } else {
            if (coinPreviouslyInserted) {
                Acceptor.stopAccepting()
            }
            coinPreviouslyInserted = false
        }

        Thread.sleep(10)
    }
}
