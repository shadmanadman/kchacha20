import uniffi.chacha20.SecretCipher

typealias EncryptedData = ByteArray

object KChaCha20 : AutoCloseable {
    private var secretCipher: SecretCipher? = null

    fun create(masterPassword: String, salt: String) {
        close()

        secretCipher = SecretCipher(masterPassword, salt)
    }

    fun encrypt(input: String): EncryptedData {
        return secretCipher?.encrypt(input)?: error("KChaCha20 not initialized. Call create first.")
    }

    fun decrypt(encryptedData: EncryptedData): String {
        return secretCipher?.decrypt(encryptedData)?: error("KChaCha20 not initialized. Call create first.")
    }

    override fun close() {
        secretCipher?.destroy()
        secretCipher = null
    }
}