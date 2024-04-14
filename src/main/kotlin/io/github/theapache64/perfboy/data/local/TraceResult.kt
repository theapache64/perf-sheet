package io.github.theapache64.perfboy.data.local

data class TraceResult(
    val name: String,
    val beforeDurationInMs: Number,
    val afterDurationInMs: Number,
    val diffInMs: Double,
    val beforeCount: Int,
    val afterCount: Int,
    val countLabel: String,
    val beforeThreadDetails: List<ThreadDetail>,
    val afterThreadDetails: List<ThreadDetail>
) {
    data class ThreadDetail(
        val threadName: String,
        var noOfBlocks : Int,
        var totalDurationInMs: Double,
        var comparison : String? = null // TODO : build
    )
}