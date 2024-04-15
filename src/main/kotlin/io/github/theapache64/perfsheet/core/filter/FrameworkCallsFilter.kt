package io.github.theapache64.perfsheet.core.filter

class FrameworkCallsFilter : Filter() {
    companion object {
        private val systemCallsRegex = listOf(
            "androidx.compose.",
            "android.",
            "com.android.internal.",
            "java.",
            "kotlinx.",
            "kotlin.",
            "sun.",
            "dalvik.",
            "Choreographer#",
            "HWUI:",
            "Compose:",
            "Recomposer:",
            "AndroidOwner:",
            "draw",
            "animation",
            "layout",
            "traversal",
            "measure",
            "Record View#draw\\(\\)"
        ).joinToString(separator = "|", prefix = "^(", postfix = ").*").toRegex()

        private val specialSystemCallsRegex = listOf(
            "android.app.ActivityThread.handleBindApplication",
            "android.app.ActivityThread.installContentProviders",
            "android.app.Activity.perform", // this will match onCreate, onStart, onResume, etc
            "androidx.lifecycle.ViewModelProvider.get", // viewModel query time

        ).joinToString(separator = "|", prefix = "^(", postfix = ").*")
            .also {
                println("QuickTag: FrameworkCallsFilter:'$it': ")
            }
            .toRegex()
    }

    override fun apply(methodName: String): String? {
        val isMatched = systemCallsRegex.matches(methodName) && !specialSystemCallsRegex.matches(methodName)
        return if (isMatched) null else methodName
    }
}