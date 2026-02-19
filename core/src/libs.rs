use chacha20poly1305::{
    AeadCore, ChaCha20Poly1305, Nonce, aead::{Aead, KeyInit, OsRng}
};
use argon2::{Argon2, password_hash::{SaltString}};

uniffi::setup_scaffolding!();

#[derive(uniffi::Object)]
pub struct SecretCipher {
    cipher: ChaCha20Poly1305,
}

#[uniffi::export]
impl SecretCipher {
    
    #[uniffi::constructor]
    pub fn new(master_password: &str, salt: &str) -> Self {
        let mut key = [0u8; 32];
        let salt_obj = SaltString::from_b64(salt).unwrap();
        
        Argon2::default()
            .hash_password_into(master_password.as_bytes(), salt_obj.as_str().as_bytes(), &mut key)
            .expect("Failed to derive key");

        let cipher = ChaCha20Poly1305::new(&key.into());
        Self { cipher }
    }

    pub fn encrypt(&self, input: &str) -> Vec<u8> {
        let nonce = ChaCha20Poly1305::generate_nonce(&mut OsRng); // 12 bytes
        let mut ciphertext = self.cipher
            .encrypt(&nonce, input.as_bytes())
            .expect("Encryption failed");
        
        let mut combined = nonce.to_vec();
        combined.append(&mut ciphertext);
        combined
    }

    pub fn decrypt(&self, encrypted_data: &[u8]) -> String {
        if encrypted_data.len() < 12 {
            panic!("Data too short");
        }

        let (nonce_bytes, ciphertext) = encrypted_data.split_at(12);
        let nonce = Nonce::from_slice(nonce_bytes);

        let decrypted = self.cipher
            .decrypt(nonce, ciphertext)
            .expect("Decryption failed - possibly wrong password or corrupted data");
            
        String::from_utf8(decrypted).expect("Invalid UTF-8 sequence")
    }
}