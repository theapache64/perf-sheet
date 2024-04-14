package io.github.theapache64.perfboy.data.repo

import io.github.theapache64.perfboy.DaggerTestAppComponent
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.BeforeEach
import java.io.File
import javax.inject.Inject

class TraceRepoImplTest {


    @Inject
    lateinit var traceRepoImpl: TraceRepoImpl

    @BeforeEach
    fun before(){
        DaggerTestAppComponent.create().inject(this)
    }

    @Test
    fun parseTest() {

        val beforeTrace = File("/Users/theapache64/Desktop/perf-boy/before.trace")
        val afterTrace = File("/Users/theapache64/Desktop/perf-boy/after.trace")

        traceRepoImpl.init(beforeTrace, afterTrace)

        val allThreadsResult = traceRepoImpl.parse(focusArea = FocusArea.ALL_THREADS)
        val mainThreadOnly = traceRepoImpl.parse(focusArea = FocusArea.MAIN_THREAD_ONLY)
        val backgroundThreadsOnly = traceRepoImpl.parse(focusArea = FocusArea.BACKGROUND_THREADS_ONLY)

        ExcelRepoImpl().make(
            File("/Users/theapache64/Desktop/perf-boy/perf-boy.xlsx"),
            allThreadsResult,
            mainThreadOnly,
            backgroundThreadsOnly
        )
        assert(allThreadsResult.isNotEmpty())
    }
}