package io.github.theapache64.perfboy.traceparser.analyzer

import io.github.theapache64.perfboy.traceparser.core.ThreadItem

class ThreadItemImpl(
    override val name: String,
    override val threadId: Int
) : ThreadItem {
    override fun toString() = name
}
