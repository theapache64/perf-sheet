package io.github.theapache64.perfboy.traceparser.core

import java.io.File

interface TraceReader {
    fun readTraceFile(traceFile: File): AnalyzerResult
}
