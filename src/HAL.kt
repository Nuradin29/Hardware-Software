import isel.leic.UsbPort
import java.io.FileInputStream
import java.io.InputStream

object HAL {
    private var isSimulation: Boolean = false
    private var deviceInitialized: Boolean = false

    // Donanımı başlatma
    fun init() {
        if (!deviceInitialized) {
            try {
                loadConfiguration()
                UsbPort.write(0) // Portu sıfırla
                println("HAL initialized. Simulation Mode: $isSimulation")
                deviceInitialized = true
            } catch (e: Exception) {
                println("Error initializing HAL: ${e.message}")
            }
        }
    }

    // Konfigürasyon Dosyasını Yükleme
    private fun loadConfiguration() {
        try {
            val properties = java.util.Properties()
            val inputStream: InputStream? = try {
                FileInputStream("USB_PORT.properties")
            } catch (e: Exception) {
                this::class.java.getResourceAsStream("/USB_PORT.properties")
            }

            inputStream?.use {
                properties.load(it)
                isSimulation = properties.getProperty("simulation", "false").lowercase() == "true"
                println("Configuration Loaded: Simulation = $isSimulation")
            } ?: throw IllegalStateException("USB_PORT.properties file not found.")
        } catch (e: Exception) {
            println("Error loading USB_PORT.properties: ${e.message}")
            isSimulation = false
        }
    }

    // Bit kontrolü: Maskedeki bit değerini kontrol et
    fun isBit(mask: Int): Boolean {
        init()
        return try {
            val portValue = UsbPort.read()
            (portValue and mask) != 0
        } catch (e: Exception) {
            println("Error reading bit: ${e.message}")
            false
        }
    }

    // Bit okuma: Maskedeki bit değerlerini oku
    fun readBits(mask: Int): Int {
        init()
        return try {
            UsbPort.read() and mask
        } catch (e: Exception) {
            println("Error reading bits: ${e.message}")
            0
        }
    }

    // Bit yazma: Maskedeki bit değerine göre ayarla
    fun writeBits(mask: Int, value: Int) {
        init()
        try {
            val current = UsbPort.read()
            val newValue = (current and mask.inv()) or (value and mask)
            UsbPort.write(newValue)
            println("Bits written: mask=$mask, value=$value, newValue=$newValue")
        } catch (e: Exception) {
            println("Error writing bits: ${e.message}")
        }
    }
}
