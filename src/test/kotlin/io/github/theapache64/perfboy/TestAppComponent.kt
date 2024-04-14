package io.github.theapache64.perfboy

import dagger.Component
import io.github.theapache64.perfboy.di.AppComponent
import io.github.theapache64.perfboy.di.module.RepoModule
import io.github.theapache64.perfboy.di.module.TraceParserModule
import io.github.theapache64.perfboy.ui.home.AnalyzeViewModel
import io.github.theapache64.perfboy.ui.home.AnalyzeViewModelTest
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
}