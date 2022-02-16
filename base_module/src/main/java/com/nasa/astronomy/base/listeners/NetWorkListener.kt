package com.nasa.astronomy.base.listeners

interface NetWorkListener {
    fun onConnectionAvailable()
    fun onConnectionLost()
}
