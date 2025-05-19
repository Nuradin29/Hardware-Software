fun main() {
    LCDMock.clear()
    LCDMock.cursor(0, 0)
    LCDMock.write("Roulette Game")
    LCDMock.cursor(1, 0)
    LCDMock.write("1 2 3 $0")
    LCDMock.printBuffer()
}
