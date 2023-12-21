package com.sesac.gmd.common

import android.util.Log
import com.sesac.gmd.BuildConfig
import java.io.PrintWriter
import java.io.StringWriter

object Logger {
    private const val TAG = "DebugLog"

    fun e() {
        if (BuildConfig.DEBUG) Log.e(TAG, buildLogMsg(""))
    }

    fun w() {
        if (BuildConfig.DEBUG) Log.w(TAG, buildLogMsg(""))
    }

    fun i() {
        if (BuildConfig.DEBUG) Log.i(TAG, buildLogMsg(""))
    }

    fun d() {
        if (BuildConfig.DEBUG) Log.d(TAG, buildLogMsg(""))
    }

    fun v() {
        if (BuildConfig.DEBUG) Log.v(TAG, buildLogMsg(""))
    }

    fun e(message: String) {
        if (BuildConfig.DEBUG) Log.e(TAG, buildLogMsg(message))
    }

    fun w(message: String) {
        if (BuildConfig.DEBUG) Log.w(TAG, buildLogMsg(message))
    }

    fun i(message: String) {
        if (BuildConfig.DEBUG) Log.i(TAG, buildLogMsg(message))
    }

    fun d(message: String) {
        if (BuildConfig.DEBUG) Log.d(TAG, buildLogMsg(message))
    }

    fun v(message: String) {
        if (BuildConfig.DEBUG) Log.v(TAG, buildLogMsg(message))
    }

    fun traceException(e: Exception) {
        if (!BuildConfig.DEBUG) return
        val errors = StringWriter()
        e.printStackTrace(PrintWriter(errors))
        e(errors.toString())
    }

    fun traceThrowable(e: Throwable) {
        if (!BuildConfig.DEBUG) return
        val errors = StringWriter()
        e.printStackTrace(PrintWriter(errors))
        Log.e(TAG, errors.toString())
    }

    private fun buildLogMsg(message: String): String {
        val ste = Thread.currentThread().stackTrace[4]
        val sb = StringBuilder().apply {
            append("[")
            append(ste.fileName.replace(".java", ""))
            append("::")
            append(ste.methodName)
            append("]")
            append(message)
        }

        return sb.toString()
    }
}