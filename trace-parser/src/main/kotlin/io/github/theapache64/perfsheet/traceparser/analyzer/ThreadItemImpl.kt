package io.github.theapache64.perfsheet.traceparser.analyzer

import io.github.theapache64.perfsheet.traceparser.core.ThreadItem

class ThreadItemImpl(
    override val name: String,
    override val threadId: Int
) : ThreadItem {
    override fun toString() = name
}
