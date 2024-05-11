package io.github.theapache64.perfsheet.core.filter


class FrameFilter : Filter() {
    override fun apply(methodName: String): String? {
        return if (methodName == "android.view.Choreographer.doFrame") methodName else null
    }
}