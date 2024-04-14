package io.github.theapache64.perfboy.ui.analyze

import com.theapache64.cyclone.core.livedata.LiveData
import com.theapache64.cyclone.core.livedata.MutableLiveData
import io.github.theapache64.perfboy.data.repo.AppRepo
import io.github.theapache64.perfboy.data.repo.ExcelRepo
import io.github.theapache64.perfboy.data.repo.ExcelRepoImpl
import io.github.theapache64.perfboy.data.repo.FocusArea
import io.github.theapache64.perfboy.data.repo.TraceRepo
import java.io.File
import javax.inject.Inject

class AnalyzeViewModel @Inject constructor(
    appRepo: AppRepo,
    private val traceRepo: TraceRepo,
    private val excelRepo: ExcelRepo
) {

    companion object{
        const val SUCCESS_MSG = "Done ✅"
    }

    private val _statusMsg = MutableLiveData<String>()
    val statusMsg: LiveData<String> = _statusMsg

    init {
        val args = appRepo.args
        when (args?.size) {
            null -> {
                _statusMsg.value = "Args missing. Try `perf-boy <before-trace> <after-trace>`"
            }

            1 -> {
                _statusMsg.value = """
                    Single trace file analysis is not implemented yet. 
                    Please vote : https://github.com/theapache64/perf-boy/issues/1
                """.trimIndent()
            }

            2 -> {
                val beforeTrace = File(args[0]).takeIf { it.exists() } ?: error("Before tr")
                val afterTrace = File(args[1]).takeIf { it.exists() } ?: error("After trace doesn't exist")
                analyzeAndCompare(beforeTrace, afterTrace)
                _statusMsg.value = SUCCESS_MSG
            }

            else -> {
                _statusMsg.value = "Can't accept more than 2 params: $args"
            }
        }
    }

    private fun analyzeAndCompare(beforeTrace: File, afterTrace: File) {
        traceRepo.init(beforeTrace, afterTrace)

        val allThreadsResult = traceRepo.parse(focusArea = FocusArea.ALL_THREADS)
        val mainThreadOnly = traceRepo.parse(focusArea = FocusArea.MAIN_THREAD_ONLY)
        val backgroundThreadsOnly = traceRepo.parse(focusArea = FocusArea.BACKGROUND_THREADS_ONLY)

        val excelFileName = "${beforeTrace.nameWithoutExtension}-vs-${afterTrace.nameWithoutExtension}.xlsx"
        val excelFile = File(beforeTrace.parent, excelFileName)
        excelRepo.make(
            excelFile,
            allThreadsResult,
            mainThreadOnly,
            backgroundThreadsOnly
        )

        _statusMsg.value = SUCCESS_MSG
    }
}