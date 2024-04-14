package io.github.theapache64.perfboy.core

import io.github.theapache64.perfboy.traceparser.core.AppLogger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TraceLogger @Inject constructor(
    var isDebug : Boolean
)  : AppLogger {
    override fun d(msg: String) {
        TODO("Not yet implemented")
    }

    override fun e(msg: String) {
        TODO("Not yet implemented")
    }

    override fun e(msg: String, t: Throwable) {
        TODO("Not yet implemented")
    }

    override fun w(msg: String) {
        TODO("Not yet implemented")
    }

    override fun i(msg: String) {
        TODO("Not yet implemented")
    }

}