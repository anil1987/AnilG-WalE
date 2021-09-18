package com.olaelectric.dataparserengine.domain

data class DBCEntity(
    val description: String,
    val compiler: String,
    val site: String,
    val messages: List<Message>
)

data class Message(
    val name: String,
    val id: Int,
    val dlc: Int,
    val signals: List<Signal>
)

data class Signal(
    val name: String,
    val startbit: Int,
    val bitlength: Int,
    val endianess: String,
    val scaling: Double,
    val offset: Int,
    val minimum: Double,
    val maximum: Double,
    val signed: Boolean,
    val floating: Int,
    val units: String
)

data class ConfigParams(
    val CAN_IDS_TO_SUBSCRIBE: List<Int>,
    val EVENT_SIGNALS: List<String>
)
