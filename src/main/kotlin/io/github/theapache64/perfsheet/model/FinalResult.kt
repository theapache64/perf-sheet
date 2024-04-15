package io.github.theapache64.perfsheet.model

data class FinalResult(
    val name: String,
    val beforeDurationInMs: Long,
    val afterDurationInMs: Long,
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