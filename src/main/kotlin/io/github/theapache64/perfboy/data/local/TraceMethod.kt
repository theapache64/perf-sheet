package io.github.theapache64.perfboy.data.local

data class TraceMethod(
    val name: String,
    val beforeDurationInMs: Long,
    val afterDurationInMs: Long,
    val diffInMs: Long,
    val beforeCount: Int,
    val afterCount: Int,
    val beforeThreadDetails: List<ThreadDetail>,
    val afterThreadDetails: List<ThreadDetail>
)

data class ThreadDetail(
    val threadName: String,
    val totalDurationInMs: Long,
    val blockCount: Int
)