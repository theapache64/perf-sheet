package io.github.theapache64.perfboy.data.repo

import io.github.theapache64.perfboy.data.local.TraceMethod
import io.github.theapache64.perfboy.traceparser.analyzer.TraceAnalyzer
import java.io.File
import javax.inject.Inject

interface TraceRepo {
    fun parse(beforeTrace: File, afterTrace: File): Map<String, TraceMethod>
}

class TraceRepoImpl @Inject constructor(
    private val traceAnalyzer: TraceAnalyzer
) : TraceRepo {
    override fun parse(beforeTrace: File, afterTrace: File): Map<String, TraceMethod> {
        val traceMap = mutableMapOf<String, TraceMethod>()

        // parse before trace first
        traceAnalyzer.analyze(beforeTrace).let { result ->

        }

        return traceMap
    }
}