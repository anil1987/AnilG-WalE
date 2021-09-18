package com.olaelectric.dataparserengine

import com.google.gson.Gson
import com.olaelectric.dataparserengine.data.CanSignalSnapshot.messageMap
import com.olaelectric.dataparserengine.data.CanSignalSnapshot.sharedVarMap
import com.olaelectric.dataparserengine.domain.DBCEntity
import com.olaelectric.dataparserengine.domain.Signal
import com.olaelectric.dataparserengine.helpers.ConfigConstructor
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

@Suppress("EXPERIMENTAL_UNSIGNED_LITERALS")
class ParserEngine(private val configConstructor: ConfigConstructor) {

    private val decForSymbols: DecimalFormatSymbols = DecimalFormatSymbols()
    private val decFormat: DecimalFormat = DecimalFormat("0.00", decForSymbols)

    fun getRoundDecimal(price: Double): Double {
        return decFormat.format(price).toDouble()
    }

    fun initEngine(
        dbcConfigJsonString: String,
        initDone: () -> Unit
    ) {
        // parsing DBC contracts
        configConstructor.constructMessageMap(
            Gson().fromJson(
                dbcConfigJsonString,
                DBCEntity::class.java
            )
        )

        // callback once the parsing is done
        initDone.invoke()
    }

    @ExperimentalUnsignedTypes
    fun parseSignal(canId: Int, data: ByteArray) {
        val canData = convertByteArrToLong(data)
        for (signal in messageMap.getOrDefault(canId, emptyList())) {
            // setting updated values in our local map based on can data
            var dataValue = if (signal.scaling != 0.0) {
                signal.scaling * genericParser(canData, signal) + signal.offset
            } else {
                genericParser(canData, signal) + signal.offset
            }
            try {
                dataValue = getRoundDecimal(dataValue)
            } catch (e: Exception) {
            }
            sharedVarMap[signal.name] = dataValue
        }
    }

    @ExperimentalUnsignedTypes
    private fun genericParser(data: ULong, signal: Signal): Double =
        ((data shr signal.startbit).and((1UL shl signal.bitlength) - 1UL)).toDouble()

    @ExperimentalUnsignedTypes
    private fun convertByteArrToLong(data: ByteArray): ULong {
        var value: ULong = 0UL
        for (i in data.indices) {
            value += data[i].toULong().and(0xffUL) shl (8 * i)
        }
        return value
    }
}
