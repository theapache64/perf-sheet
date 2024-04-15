package io.github.theapache64.perfsheet.traceparser.analyzer

import io.github.theapache64.perfsheet.traceparser.core.ThreadTimeBounds

data class ThreadTimeBoundsImpl(
    override var minTime: Double = Double.MAX_VALUE,
    override var maxTime: Double = 0.0
) : ThreadTimeBounds
