package io.github.theapache64.perfsheet.traceparser.analyzer.converter

/**
 * Converts class names and class methods names.
 * For example converts obfuscated names to original names.
 */
interface NameConverter {
    fun convertClassName(sourceClassName: String): String
    fun convertMethodName(className: String, sourceMethodName: String, type: String?): String
}