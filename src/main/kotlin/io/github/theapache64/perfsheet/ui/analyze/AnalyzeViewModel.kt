package io.github.theapache64.perfsheet.ui.analyze

import com.theapache64.cyclone.core.livedata.LiveData
import com.theapache64.cyclone.core.livedata.MutableLiveData
import io.github.theapache64.perfsheet.data.repo.AppRepo
import io.github.theapache64.perfsheet.data.repo.ExcelRepo
import io.github.theapache64.perfsheet.data.repo.FocusArea
import io.github.theapache64.perfsheet.data.repo.TraceRepo
import java.io.File
import javax.inject.Inject

class AnalyzeViewModel @Inject constructor(
    private val appRepo: AppRepo,
    private val traceRepo: TraceRepo,
    private val excelRepo: ExcelRepo
) {

    companion object {
        const val SUCCESS_MSG = "Done ✅"
    }

    private val _statusMsg = MutableLiveData<String>()
    val statusMsg: LiveData<String> = _statusMsg

    fun init() {
        val args = appRepo.args
        when (args?.size) {
            null, 0 -> {
                _statusMsg.value = "Args missing. Try `perf-sheet <before-trace> <after-trace>`"
            }

            1 -> {
                _statusMsg.value = """
                    Single trace file analysis is not implemented yet. 
                    Please vote : https://github.com/theapache64/perf-sheet/issues/1
                """.trimIndent()
            }

            2 -> {
                val beforeTrace = File(args[0]).takeIf { it.exists() } ?: error("Before trace doesn't exist")
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
        val onProgress: (String) -> Unit = {
            _statusMsg.value = it
        }

        _statusMsg.value = "📖 Parsing traces... (this step may take a while)"
        traceRepo.init(beforeTrace, afterTrace, onProgress)
        _statusMsg.value = "🔍 Comparing traces..."

        val allThreads = traceRepo.parse(focusArea = FocusArea.ALL_THREADS)
        val mainThread = traceRepo.parse(focusArea = FocusArea.MAIN_THREAD_ONLY)
        val backgroundThreads = traceRepo.parse(focusArea = FocusArea.BACKGROUND_THREADS_ONLY)
        val allThreadsMinified = traceRepo.parse(focusArea = FocusArea.ALL_THREADS_MINIFIED)
        val mainThreadMinified = traceRepo.parse(focusArea = FocusArea.MAIN_THREAD_MINIFIED)

        val excelFileName = "${beforeTrace.nameWithoutExtension}-vs-${afterTrace.nameWithoutExtension}.xlsx"
        val excelFile = File(beforeTrace.parent, excelFileName)
        _statusMsg.value = "📝 Writing to spreadsheet (${excelFile.name})... "

        excelRepo.make(
            excelFile,
            allThreads,
            mainThread,
            backgroundThreads,
            allThreadsMinified,
            mainThreadMinified,
            onProgress = onProgress
        )
    }
}