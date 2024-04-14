package io.github.theapache64.perfboy.model

data class FinalResult(
    val name: String,
    val beforeDurationInMs: String,
    val afterDurationInMs: String,
    val diffInMs: Long,
    val beforeCount: Int,
    val afterCount: Int,
    val countComparison: String,
    val beforeThreadDetails: List<ThreadDetail>,
    val afterThreadDetails: List<ThreadDetail>,
    var beforeComparison: String?,
    var afterComparison: String?
) {
    data class ThreadDetail(
        val threadName: String,
        var noOfBlocks: Int,
        var totalDurationInMs: Double,
    )
}