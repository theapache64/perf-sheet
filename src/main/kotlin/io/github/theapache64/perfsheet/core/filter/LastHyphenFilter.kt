package io.github.theapache64.perfsheet.core.filter
import io.github.theapache64.perfsheet.model.Method

class LastHyphenFilter: Filter() {
    override fun apply(methodName: String): String {
        return removeLastHyphen(methodName)
    }

    private fun removeLastHyphen(name: String): String {
        val hyphenIndex = name.lastIndexOf('-')
        if (hyphenIndex == -1) return name
        return name.substring(0, hyphenIndex)
    }
}