package io.github.theapache64.perfsheet

import dagger.Component
import io.github.theapache64.perfsheet.data.repo.TraceRepoImplTest
import io.github.theapache64.perfsheet.di.module.RepoModule
import io.github.theapache64.perfsheet.di.module.TraceParserModule
import io.github.theapache64.perfsheet.ui.analyze.AnalyzeViewModelTest
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        RepoModule::class,
        TraceParserModule::class
    ]
)
interface TestAppComponent {
    fun inject(analyzeViewModelTest: AnalyzeViewModelTest)
    fun inject(traceRepoImplTest: TraceRepoImplTest)
}