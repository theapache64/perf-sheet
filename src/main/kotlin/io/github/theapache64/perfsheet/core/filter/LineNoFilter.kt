package io.github.theapache64.perfsheet.core.filter

class LineNoFilter : Filter() {
    companion object {
        private val lineNoRegEx = "^(?<title>.+) (?<lineNo>\\(.+:\\d+\\))\$".toRegex()
    }

    private fun removeLineNoFromRowName(name: String): String {
        val result = lineNoRegEx.find(name)
        var newName = name
        if (result != null) {
            newName = result.groupValues.getOrNull(1) ?: name
        }

        if (newName.contains("$1")) {
            newName = newName.substring(0, newName.indexOf("\$1"))
        }

        return newName
    }

    override fun apply(methodName: String): String {
        return removeLineNoFromRowName(methodName)
    }
}