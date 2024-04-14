package io.github.theapache64.perfboy.ui.home

import com.theapache64.cyclone.core.livedata.LiveData
import com.theapache64.cyclone.core.livedata.MutableLiveData
import io.github.theapache64.perfboy.data.repo.AppRepo
import java.io.File
import javax.inject.Inject

class AnalyzeViewModel @Inject constructor(
    appRepo: AppRepo
) {
    private val _statusMsg = MutableLiveData<String>()
    val statusMsg: LiveData<String> = _statusMsg

    init {
        val args = appRepo.args
        when (args?.size) {
            1 -> TODO(
                """
                Single trace file analysis is not implemented yet. 
                Please vote : https://github.com/theapache64/perf-boy/issues/1
            """.trimIndent()
            )

            2 -> {
                val beforeTrace = File(args[0]).takeIf { it.exists() } ?: error("Before tr")
                val afterTrace = File(args[1]).takeIf { it.exists() } ?: error("After trace doesn't exist")
                analyzeAndCompare(beforeTrace, afterTrace)
            }

            else -> throw IllegalStateException("Can't accept more than 2 params")
        }


    }

    private fun analyzeAndCompare(beforeTrace: File, afterTrace: File) {

    }
}