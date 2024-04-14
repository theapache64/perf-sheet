package io.github.theapache64.perfboy.data.repo

import io.github.theapache64.perfboy.DaggerTestAppComponent
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

        val beforeTrace = File("/Users/theapache64/Desktop/perf-boy/before.trace")
        val afterTrace = File("/Users/theapache64/Desktop/perf-boy/after.trace")

        traceRepo.init(beforeTrace, afterTrace)

        val allThreadsResult = traceRepo.parse(focusArea = FocusArea.ALL_THREADS)
        val mainThreadOnly = traceRepo.parse(focusArea = FocusArea.MAIN_THREAD_ONLY)
        val backgroundThreadsOnly = traceRepo.parse(focusArea = FocusArea.BACKGROUND_THREADS_ONLY)

        excelRepo.make(
            File("/Users/theapache64/Desktop/perf-boy/perf-boy.xlsx"),
            allThreadsResult,
            mainThreadOnly,
            backgroundThreadsOnly,
            onProgress = {
                println(it)
            }
        )
        assert(allThreadsResult.isNotEmpty())
    }
}