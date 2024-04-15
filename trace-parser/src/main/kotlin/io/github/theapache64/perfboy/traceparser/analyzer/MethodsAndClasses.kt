package io.github.theapache64.perfsheet.traceparser.analyzer

class MethodsAndClasses {
    private val emptyData = MethodData("", "", "")
    private val data = mutableMapOf<Long, MethodData>()

    fun put(id: Long, newElement: MethodData) {
        data[id] = newElement
    }

    fun getFullNameById(id: Long): String {
        return data.getOrDefault(id, emptyData).fullName
    }
}
