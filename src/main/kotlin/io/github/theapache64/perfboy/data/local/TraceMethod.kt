package io.github.theapache64.perfboy.data.local

data class TraceMethod(
    val name: String,
    val threadDetail: MutableList<ThreadDetail>,
)

data class ThreadDetail(
    val threadName: String,
    val totalDurationInMs: Double
)