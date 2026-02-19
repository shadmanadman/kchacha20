This repository is part of the example accompanying the article:

Rust + KMP: System Performance Meets Multiplatform Delivery
https://medium.com/@adman.shadman/rust-kmp-system-performance-meets-multiplatform-delivery-b403c2a4f6fc

## Use this lib:
Add it to your dependencies:

``` io.github.shadmanadman:kchacha20lib:0.5.8 ```

Call create on `KChaCha20`. give it a masterPassword and your salt. this will be used as your cipher password:
```
//each time you call create the previous instance will be destroyed
KChaCha20.create(masterPassword,salt)

// you can also call destroy menually
KChaCha20.destroy()
```

Encrypt your data:
```KChaCha20.encrypt(input: String):ByteArray```

Decrypt the data:
```KChaCha20.decrypt(encryptedData: ByteArray)```