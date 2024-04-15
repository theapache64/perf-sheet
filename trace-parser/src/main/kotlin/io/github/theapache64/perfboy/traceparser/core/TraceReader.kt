package io.github.theapache64.perfsheet.traceparser.core

import java.io.File

interface TraceReader {
    fun readTraceFile(traceFile: File): AnalyzerResult
}
