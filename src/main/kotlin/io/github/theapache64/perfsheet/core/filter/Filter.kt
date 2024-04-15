package io.github.theapache64.perfsheet.core.filter

abstract class Filter {
    abstract fun apply(methodName: String): String?
}