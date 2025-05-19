object Acceptor {
    fun isCoinInserted() = HAL.isBit(Masks.I6)
    fun coinID() = if (HAL.isBit(Masks.I5)) 1 else 0

    fun acceptCoin() {
        HAL.setBits(Masks.O6)
    }

    fun stopAccepting() {
        HAL.clrBits(Masks.O6)
    }
}
