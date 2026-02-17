use chacha20poly1305::{
    AeadCore, ChaCha20Poly1305, Nonce, aead::{Aead, KeyInit, OsRng}
};
use argon2::{Argon2, password_hash::{SaltString}};
use serde::{Serialize, Deserialize};

uniffi::setup_scaffolding!();

#[derive(Serialize, Deserialize, Debug)]
pub struct SecretEntry {
    pub service: String,
    pub username: String,
    pub password: Vec<u8>,
}

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
        let  nonce = ChaCha20Poly1305::generate_nonce(&mut OsRng);
        self.cipher.encrypt(&nonce, input.as_bytes()).expect("Encryption failed")
    }

    pub fn decrypt(&self, encrypted_data: &[u8]) -> String {
        let (nonce_bytes, _) = encrypted_data.split_at(12);
        let nonce = Nonce::from_slice(nonce_bytes);

        let decrypted = self.cipher.decrypt(nonce, encrypted_data).expect("Decryption failed");
        String::from_utf8(decrypted).unwrap()
    }
}

