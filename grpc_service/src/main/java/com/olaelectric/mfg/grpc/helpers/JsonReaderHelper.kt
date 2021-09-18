package com.olaelectric.mfg.grpc.helpers

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.Reader
import java.io.StringWriter
import java.io.Writer

class JsonReaderHelper(private val context: Context, private val rawResId: Int) {

    companion object {
        private const val BUFFER_SIZE = 1024
        private const val CHAR_SET_NAME = "UTF-8"
    }

    fun readJsonFromAssetsFolder(): String {
        val raw = context.resources.openRawResource(rawResId)
        val writer: Writer = StringWriter()
        val buffer = CharArray(BUFFER_SIZE)
        raw.use { rawData ->
            val reader: Reader = BufferedReader(InputStreamReader(rawData, CHAR_SET_NAME))
            var n: Int
            while (reader.read(buffer).also { n = it } != -1) {
                writer.write(buffer, 0, n)
            }
        }
        return writer.toString()
    }
}
