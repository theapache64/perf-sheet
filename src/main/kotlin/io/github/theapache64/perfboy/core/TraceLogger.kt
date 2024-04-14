package io.github.theapache64.perfboy.core

import io.github.theapache64.perfboy.traceparser.core.AppLogger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TraceLogger @Inject constructor(
    var isDebug: Boolean
) : AppLogger {
    override fun d(msg: String) {
        if (isDebug) {
            println("debug: $msg")
        }
    }

    override fun e(msg: String) {
        if (isDebug) {
            println("error: $msg")
        }
    }

    override fun e(msg: String, t: Throwable) {
        if (isDebug) {
            println("error: $msg : ${t.message}")
        }
    }

    override fun w(msg: String) {
        if (isDebug) {
            println("warning: $msg")
        }
    }

    override fun i(msg: String) {
        if (isDebug) {
            println("info: $msg")
        }
    }
}