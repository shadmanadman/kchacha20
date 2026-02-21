This repository is part of the example accompanying the following article:

Rust + KMP: System Performance Meets Multiplatform Delivery
https://medium.com/@adman.shadman/rust-kmp-system-performance-meets-multiplatform-delivery-b403c2a4f6fc

## Use this lib:
Add it to your dependencies:

``` 
io.github.shadmanadman:kchacha20lib:0.5.8
```

Call create on `KChaCha20`. give it a masterPassword and your salt. this will be used as your cipher password:
```kotlin
/** 
 * each time you call create the previous instance will be destroyed.
 *`use {}` ensures deterministic cleanup by automatically calling `close()`
 * at the end of the block.
 * AutoCloseable defines a single `close()` method and enables structured
 * resource management similar to try-with-resources in Java.
 * Checkout here:
https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-auto-closeable.html
 **/
KChaCha20.use{
 it.create(masterPassword,salt)
}

/**
 * you can also call close manually. 
 */
KChaCha20.close()
```

Encrypt your data:
```kotlin
KChaCha20.use{
it.encrypt(input: String):ByteArray
}
```

Decrypt the data:
```kotlin
KChaCha20.use{
it.decrypt(encryptedData: ByteArray):String
}
```