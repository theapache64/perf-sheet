package io.github.theapache64.perfboy.traceparser.analyzer.converter

object NoOpNameConverter : NameConverter {
    override fun convertClassName(sourceClassName: String) = sourceClassName

    override fun convertMethodName(className: String, sourceMethodName: String, type: String?) = sourceMethodName
}