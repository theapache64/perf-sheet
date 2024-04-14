package io.github.theapache64.perfboy.data.local

data class TraceMethod(
    val name: String,
    val threadNodes: MutableList<ThreadNode>,
)

data class ThreadNode(
    val threadName: String,
    val durationInMs: Double
)