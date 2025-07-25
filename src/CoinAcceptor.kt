object CoinAcceptor {
    private var previousState = false

    fun isCoinInserted(): Boolean {
        val currentState = HAL.isBit(Masks.I6)
        val inserted = !previousState && currentState
        previousState = currentState
        return inserted
    }

    fun coinID(): Int {
        return if (HAL.isBit(Masks.I5)) 1 else 0
    }

    fun acceptCoin(): Boolean {
        return if (coinID() == 1) {
            HAL.setBits(Masks.O6)
            true
        } else {
            false
        }
    }

    fun stopAccepting() {
        HAL.clrBits(Masks.O6)
    }


}
