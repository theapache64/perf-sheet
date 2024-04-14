package io.github.theapache64.perfboy.data.repo

import io.github.theapache64.perfboy.data.local.ThreadNode
import io.github.theapache64.perfboy.data.local.TraceMethod
import io.github.theapache64.perfboy.data.local.TraceResult
import io.github.theapache64.perfboy.traceparser.analyzer.TraceAnalyzer
import io.github.theapache64.perfboy.traceparser.core.AnalyzerResult
import java.io.File
import javax.inject.Inject

interface TraceRepo {
    fun parse(beforeTrace: File, afterTrace: File): Map<String, TraceResult>
}

class TraceRepoImpl @Inject constructor(
    private val traceAnalyzer: TraceAnalyzer
) : TraceRepo {
    override fun parse(beforeTrace: File, afterTrace: File): Map<String, TraceResult> {
        val traceResult = mutableMapOf<String, TraceResult>()
        // parse before trace first
        val beforeMap = traceAnalyzer.analyze(beforeTrace).toMap()
        val afterMap = traceAnalyzer.analyze(afterTrace).toMap()

        val methodNames = beforeMap.keys + afterMap.keys
        for (methodName in methodNames) {
            val beforeMethod = beforeMap[methodName]
            val afterMethod = afterMap[methodName]
            val diffInMs = calculateCountDiff(beforeMethod, afterMethod)

            val beforeCount = beforeMethod?.threadNodes?.size ?: -1
            val afterCount = afterMethod?.threadNodes?.size ?: -1
            val countLabel = calculateCountLabel(beforeCount, afterCount)

            val beforeThreadDetails = calculateThreadDetails(beforeMethod)
            val afterThreadDetails = calculateThreadDetails(afterMethod)


            traceResult[methodName] = TraceResult(
                name = methodName,
                beforeDurationInMs = beforeMethod?.threadNodes?.sumOf { it.durationInMs } ?: -1,
                afterDurationInMs = afterMethod?.threadNodes?.sumOf { it.durationInMs } ?: -1,
                diffInMs = diffInMs,
                beforeCount = beforeCount,
                afterCount = afterCount,
                countLabel = countLabel,
                beforeThreadDetails = beforeThreadDetails,
                afterThreadDetails = afterThreadDetails
            )
        }
        return traceResult.entries.sortedByDescending { it.value.diffInMs }.associateBy({ it.key }, { it.value })
    }

    private fun calculateThreadDetails(beforeMethod: TraceMethod?): List<TraceResult.ThreadDetail> {
        val threadDetails = mutableListOf<TraceResult.ThreadDetail>()
        for (threadNode in beforeMethod?.threadNodes ?: emptyList()) {
            var threadDetail = threadDetails.find { it.threadName == threadNode.threadName }
            if (threadDetail == null) {
                // first detail node
                threadDetail = TraceResult.ThreadDetail(threadNode.threadName, noOfBlocks = 0, 0.0)
                threadDetails.add(threadDetail)
            }
            threadDetail.noOfBlocks++
            threadDetail.totalDurationInMs += threadNode.durationInMs
        }

        // compare by total duration but if there's threadName == "main", it should be always first
        return threadDetails.sortedByDescending {
            if (it.threadName == "main") {
                Long.MAX_VALUE
            } else {
                it.totalDurationInMs.toLong()
            }
        }
    }

    private fun calculateCountLabel(beforeCount: Int, afterCount: Int): String {
        return when {
            beforeCount == afterCount -> "0"
            beforeCount > 0 && afterCount == -1 -> "Removed ($beforeCount)"
            beforeCount == -1 && afterCount > 0 -> "Added ($afterCount)"
            else -> {
                val diff = (afterCount - beforeCount)
                when {
                    diff > 0 -> "$diff (added)"
                    else -> "$diff (removed)"
                }
            }
        }
    }

    private fun calculateCountDiff(beforeMethod: TraceMethod?, afterMethod: TraceMethod?): Double {
        return when {
            beforeMethod != null && afterMethod != null -> {
                afterMethod.threadNodes.sumOf {
                    it.durationInMs
                } - beforeMethod.threadNodes.sumOf {
                    it.durationInMs
                }
            }

            afterMethod != null -> {
                afterMethod.threadNodes.sumOf { it.durationInMs }
            }

            beforeMethod != null -> {
                -beforeMethod.threadNodes.sumOf { it.durationInMs }
            }

            else -> 0.0
        }
    }

    private fun AnalyzerResult.toMap(): Map<String, TraceMethod> {
        val resultMap = mutableMapOf<String, TraceMethod>()
        for ((threadId, allMethods) in this.data) {
            val thread = this.threads.find { it.threadId == threadId } ?: error("Thread not found: '$threadId'")
            for (method in allMethods) {
                val methodName = method.name
                val traceMethod = resultMap.getOrPut(
                    methodName
                ) {
                    TraceMethod(
                        name = methodName,
                        threadNodes = mutableListOf()
                    )
                }

                val duration = method.threadEndTimeInMillisecond - method.threadStartTimeInMillisecond
                traceMethod.threadNodes.add(
                    ThreadNode(
                        threadName = thread.name,
                        durationInMs = duration
                    )
                )
            }
        }

        return resultMap
    }
}