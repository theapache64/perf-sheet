package io.github.theapache64.perfsheet.traceparser.core

interface AnalyzerResult {
    val threadTimeBounds: Map<Int, ThreadTimeBounds>
    val globalTimeBounds: Map<Int, ThreadTimeBounds>
    val maxLevel: Int
    val data: Map<Int, List<ProfileData>>
    val threads: List<ThreadItem>
    val mainThreadId: Int
    val startTimeUs: Long // start recording time in System.upTimeInMs()
}
