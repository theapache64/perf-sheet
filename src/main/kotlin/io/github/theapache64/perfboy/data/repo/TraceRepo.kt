package io.github.theapache64.perfboy.data.repo

import io.github.theapache64.perfboy.data.local.ThreadDetail
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
            val diffInMs = getDiffInMs(beforeMethod, afterMethod)

            val beforeCount = beforeMethod?.threadDetail?.size ?: -1
            val afterCount = afterMethod?.threadDetail?.size ?: -1
            val countLabel = getCountLabel(beforeCount, afterCount)

            traceResult[methodName] = TraceResult(
                name = methodName,
                beforeDurationInMs = beforeMethod?.threadDetail?.sumOf { it.totalDurationInMs } ?: -1,
                afterDurationInMs = afterMethod?.threadDetail?.sumOf { it.totalDurationInMs } ?: -1,
                diffInMs = diffInMs,
                beforeCount = beforeCount,
                afterCount = afterCount,
                countLabel = countLabel,
                beforeThreadDetails = listOf(),
                afterThreadDetails = listOf()
            )
        }
        return traceResult.entries.sortedByDescending { it.value.diffInMs }.associateBy({ it.key }, { it.value })
    }

    private fun getCountLabel(beforeCount: Int, afterCount: Int): String {
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

    private fun getDiffInMs(beforeMethod: TraceMethod?, afterMethod: TraceMethod?): Double {
        return when {
            beforeMethod != null && afterMethod != null -> {
                afterMethod.threadDetail.sumOf {
                    it.totalDurationInMs
                } - beforeMethod.threadDetail.sumOf {
                    it.totalDurationInMs
                }
            }

            afterMethod != null -> {
                afterMethod.threadDetail.sumOf { it.totalDurationInMs }
            }

            beforeMethod != null -> {
                -beforeMethod.threadDetail.sumOf { it.totalDurationInMs}
            }

            else -> -1.0
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
                        threadDetail = mutableListOf()
                    )
                }

                val duration = method.threadEndTimeInMillisecond - method.threadStartTimeInMillisecond
                traceMethod.threadDetail.add(
                    ThreadDetail(
                        threadName = thread.name,
                        totalDurationInMs = duration
                    )
                )
            }
        }

        return resultMap
    }
}