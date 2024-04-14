package io.github.theapache64.perfboy.ui.analyze

import io.github.theapache64.perfboy.DaggerTestAppComponent
import io.github.theapache64.perfboy.data.repo.AppRepo
import io.github.theapache64.perfboy.data.repo.ExcelRepo
import io.github.theapache64.perfboy.data.repo.TraceRepo
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.inject.Inject


class AnalyzeViewModelTest {

    @Inject
    lateinit var analyzeViewModel: AnalyzeViewModel

    @Inject
    lateinit var traceRepo: TraceRepo

    @Inject
    lateinit var excelRepo: ExcelRepo

    @BeforeEach
    fun before() {
        DaggerTestAppComponent.create().inject(this)

        val fakeAppRepo = mockk<AppRepo>()
        every { fakeAppRepo.args } returns listOf(
            "/Users/theapache64/Desktop/perf-boy/before.trace", // TODO: add some other traces to repo
            "/Users/theapache64/Desktop/perf-boy/after.trace"
        )
        analyzeViewModel = AnalyzeViewModel(fakeAppRepo, traceRepo, excelRepo)
    }

    @Test
    fun parseTest() {
        analyzeViewModel.statusMsg.observe {
            assertEquals(AnalyzeViewModel.SUCCESS_MSG, it)
        }
    }
}