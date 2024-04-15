package io.github.theapache64.perfsheet.core.filter


class AnonFilter : Filter() {
    private fun removeAnon(name: String): String {
        return name.replace(".<anonymous>", "")
    }

    override fun apply(methodName: String): String {
        return removeAnon(methodName)
    }
}