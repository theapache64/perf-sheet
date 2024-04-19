package io.github.theapache64.perfsheet.core.filter

class FrameworkCallsFilter : Filter() {
    companion object {
        private val blackList = listOf(
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

        private val whiteList = listOf(
            "android.app.ActivityThread.handleBindApplication",
            "android.app.ActivityThread.installContentProviders",
            "android.app.Activity.perform", // this will match onCreate, onStart, onResume, etc
            "androidx.lifecycle.ViewModelProvider.get", // viewModel query time
        ).joinToString(separator = "|", prefix = "^(", postfix = ").*")
            .toRegex()
    }

    override fun apply(methodName: String): String? {
        val shouldSkipMethodName = blackList.matches(methodName) && !whiteList.matches(methodName)
        return if (shouldSkipMethodName) null else methodName
    }
}