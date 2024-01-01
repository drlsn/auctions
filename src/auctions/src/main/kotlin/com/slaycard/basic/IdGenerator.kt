package com.slaycard.basic

import java.nio.ByteBuffer
import java.util.*

fun uuid64(): String = UUID.randomUUID().toBase64()
fun uuid(): String = UUID.randomUUID().toString()

fun UUID.toBase64(): String {
    val byteArray = ByteBuffer.allocate(16)
        .putLong(this.mostSignificantBits)
        .putLong(this.leastSignificantBits)
        .array()

    val base64 = String(Base64.getEncoder().encode(byteArray)).removeSuffix("==")
    return String(base64.map{ customEncode(it) }.toCharArray())
}

fun customEncode(char: Char): Char {
    return when (char) {
        in 'A'..'Z', in 'a'..'z', in '0'..'9' -> char
        else -> '0' + (char.toInt() % 36)  // Custom encoding for other characters
    }
}
