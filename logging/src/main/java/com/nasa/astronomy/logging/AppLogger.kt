package com.nasa.astronomy.logging

import android.util.Log

object AppLogger {

    private val isDebugSettingsEnabled
        get() = BuildConfig.DEBUG

    fun v(tag: String, msg: String): Int {
        return if (isDebugSettingsEnabled) {
            Log.v(tag, msg)
        } else 0
    }

    fun v(tag: String, msg: String, tr: Throwable): Int {
        return if (isDebugSettingsEnabled) {
            Log.v(tag, msg, tr)
        } else 0
    }

    fun d(tag: String, msg: String): Int {
        return if (isDebugSettingsEnabled) {
            Log.d(tag, msg)
        } else 0
    }

    fun d(tag: String, msg: String, tr: Throwable): Int {
        return if (isDebugSettingsEnabled) {
            Log.d(tag, msg, tr)
        } else 0
    }

    fun i(tag: String, msg: String): Int {
        return if (isDebugSettingsEnabled) {
            Log.i(tag, msg)
        } else 0
    }

    fun i(tag: String, msg: String, tr: Throwable): Int {
        return if (isDebugSettingsEnabled) {
            Log.i(tag, msg, tr)
        } else 0
    }

    fun w(tag: String, msg: String): Int {
        return if (isDebugSettingsEnabled) {
            Log.w(tag, msg)
        } else 0
    }

    fun w(tag: String, msg: String, tr: Throwable): Int {
        return if (isDebugSettingsEnabled) {
            Log.w(tag, msg, tr)
        } else 0
    }

    fun w(tag: String, tr: Throwable): Int {
        return if (isDebugSettingsEnabled) {
            Log.w(tag, tr)
        } else 0
    }

    fun e(tag: String, msg: String): Int {
        return if (isDebugSettingsEnabled) {
            Log.e(tag, msg)
        } else 0
    }

    fun e(tag: String, msg: String, tr: Throwable): Int {
        return if (isDebugSettingsEnabled) {
            Log.e(tag, msg, tr)
        } else 0
    }

    fun println(priority: Int, tag: String, msg: String): Int {
        return if (isDebugSettingsEnabled) {
            Log.println(priority, tag, msg)
        } else 0
    }
}
