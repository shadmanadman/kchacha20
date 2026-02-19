import uniffi.chacha20.SecretCipher

typealias EncryptedData = ByteArray

object KChaCha20 {
    private var secretCipher: SecretCipher? = null

    fun create(masterPassword: String, salt: String) {
        destroy()

        secretCipher = SecretCipher(masterPassword, salt)
    }

    fun encrypt(input: String): EncryptedData {
        return secretCipher?.encrypt(input)?: error("KChaCha20 not initialized")
    }

    fun decrypt(encryptedData: EncryptedData): String {
        return secretCipher?.decrypt(encryptedData)?: error("KChaCha20 not initialized")
    }

    fun destroy(){
        secretCipher?.destroy()
        secretCipher = null
    }
}