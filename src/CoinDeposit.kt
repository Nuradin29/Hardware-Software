class CoinDeposit {
    var balance: Int = 0
    var totalCoins: Int = 0

    fun deposit() {
        val credit = 2
        balance += credit
        totalCoins += credit

    }

    fun useCredit(amount: Int): Boolean {
        return if (balance >= amount) {
            balance -= amount
            true
        } else false
    }

    fun reset() {
        balance = 0
        totalCoins = 0
    }
}
