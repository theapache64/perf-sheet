package io.github.theapache64.perfsheet.data.repo

import io.github.theapache64.perfsheet.DaggerTestAppComponent
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import javax.inject.Inject

class TraceRepoImplTest {


    @Inject
    lateinit var traceRepo: TraceRepo

    @Inject
    lateinit var excelRepo: ExcelRepo

    @BeforeEach
    fun before() {
        DaggerTestAppComponent.create().inject(this)
    }

    @Test
    fun parseTest() {

        val beforeTrace = File("/Users/theapache64/Desktop/bad-app/before.trace")
        val afterTrace = File("/Users/theapache64/Desktop/bad-app/after.trace")

        traceRepo.init(beforeTrace, afterTrace) {
            println("QuickTag: TraceRepoImplTest:parseTest: $it")
        }

        val allThreadsResult = traceRepo.parse(focusArea = FocusArea.ALL_THREADS)
        val allThreadsMinifiedResult = traceRepo.parse(focusArea = FocusArea.ALL_THREADS_MINIFIED)
        val mainThreadOnly = traceRepo.parse(focusArea = FocusArea.MAIN_THREAD_ONLY)
        val backgroundThreadsOnly = traceRepo.parse(focusArea = FocusArea.BACKGROUND_THREADS_ONLY)
        val mainThreadMinified = traceRepo.parse(focusArea = FocusArea.MAIN_THREAD_MINIFIED)
        val frames = traceRepo.parse(focusArea = FocusArea.FRAMES)

        excelRepo.make(
            xlsFile = File("/Users/theapache64/Desktop/bad-app/perf-sheet.xlsx"),
            isSingle = false,
            allThreadData = allThreadsResult,
            mainThreadData = mainThreadOnly,
            backgroundThreadData = backgroundThreadsOnly,
            allThreadDataMinified = allThreadsMinifiedResult,
            mainThreadMinified = mainThreadMinified,
            frames = frames,
            onProgress = {
                println(it)
            }
        )
        assert(allThreadsResult.isNotEmpty())
        assert(mainThreadOnly.isNotEmpty())
        assert(backgroundThreadsOnly.isNotEmpty())
        assert(allThreadsMinifiedResult.isNotEmpty())
        assert(mainThreadMinified.isNotEmpty())
        assert(frames.isNotEmpty())
    }
}