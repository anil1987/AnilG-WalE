package com.olaelectric.dataparserengine.can

import com.olaelectric.mfg.logging.AppLogger

object ByteHelper {

    private val TAG = ByteHelper::class.simpleName.toString()

    fun decodeHexString(hexString: String): ByteArray {
        require(hexString.length % 2 != 1) { "Invalid hexadecimal String supplied." }
        val bytes = ByteArray(hexString.length / 2)
        var i = 0
        while (i < hexString.length) {
            bytes[i / 2] = hexToByte(hexString.substring(i, i + 2))
            i += 2
        }
        return bytes
    }

    private fun hexToByte(hexString: String): Byte {
        val firstDigit = toDigit(hexString[0])
        val secondDigit = toDigit(hexString[1])
        return ((firstDigit shl 4) + secondDigit).toByte()
    }

    private fun toDigit(hexChar: Char): Int {
        val digit = Character.digit(hexChar, 16)
        require(digit != -1) { "Invalid Hexadecimal Character: $hexChar" }
        return digit
    }

    fun getCanId(byteArray: ByteArray): Int {
        val iterator = byteArray.iterator()
        var temp = ""
        if (byteArray.size == 18) {
            iterator.next()
            iterator.next()
        }
        for (i in 3 downTo 0) {
            temp += String.format("%02X", iterator.next())
        }
        AppLogger.i(TAG, "Can id from frame: ${Integer.parseInt(temp, 16)}")
        return Integer.parseInt(temp, 16)
    }
}
