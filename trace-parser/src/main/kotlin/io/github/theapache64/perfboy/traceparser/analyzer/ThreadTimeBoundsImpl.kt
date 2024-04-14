package io.github.theapache64.perfboy.traceparser.analyzer

import io.github.theapache64.perfboy.traceparser.core.ThreadTimeBounds

data class ThreadTimeBoundsImpl(
    override var minTime: Double = Double.MAX_VALUE,
    override var maxTime: Double = 0.0
) : ThreadTimeBounds
