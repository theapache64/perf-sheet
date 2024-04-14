package io.github.theapache64.perfboy

import dagger.Component
import io.github.theapache64.perfboy.data.repo.TraceRepoImplTest
import io.github.theapache64.perfboy.di.module.RepoModule
import io.github.theapache64.perfboy.di.module.TraceParserModule
import io.github.theapache64.perfboy.ui.analyze.AnalyzeViewModelTest
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