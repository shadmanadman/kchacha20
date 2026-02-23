This repository is part of the example accompanying the following article:

[Rust + KMP: System Performance Meets Multiplatform Delivery](https://medium.com/@adman.shadman/rust-kmp-system-performance-meets-multiplatform-delivery-b403c2a4f6fc)


kchacha20 is a Kotlin Multiplatform wrapper around a rust authenticated encryption implementation built on [chacha20poly1305](https://docs.rs/chacha20poly1305/latest/chacha20poly1305/).
The cryptographic operations (key derivation and AEAD encryption/decryption) are executed in native Rust for performance and memory safety,
while Kotlin exposes a structured, AutoCloseable API to ensure effective cleanup of native resources.

## Use this lib:
Add it to your dependencies:

``` 
io.github.shadmanadman:kchacha20lib:0.20.6
```

Call create on `KChaCha20`. give it a masterPassword and your salt. this will be used as your cipher password:
```kotlin
/** 
 * each time you call create the previous instance will be destroyed.
 *`use {}` ensures cleanup by automatically calling `close()` at the end of the block.
 * AutoCloseable defines a single `close()` method and enables structured
 * resource management similar to try-with-resources in java.
 * Checkout here:
 * https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-auto-closeable.html
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



```
Copyright (c) 2025 KSensor

Permission to use, copy, modify, and/or distribute this software for any purpose
with or without fee is hereby granted.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
```
