package io.github.theapache64.perfsheet.di

import dagger.Component
import io.github.theapache64.perfsheet.app.App
import io.github.theapache64.perfsheet.di.module.RepoModule
import io.github.theapache64.perfsheet.di.module.TraceParserModule
import io.github.theapache64.perfsheet.ui.analyze.AnalyzeActivity
import io.github.theapache64.perfsheet.ui.splash.SplashActivity
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        RepoModule::class,
        TraceParserModule::class
    ]
)
interface AppComponent {
    fun inject(app: App)
    fun inject(splashActivity: SplashActivity)
    fun inject(analyzeActivity: AnalyzeActivity)
}


