package io.github.theapache64.perfboy.di

import dagger.Component
import io.github.theapache64.perfboy.app.App
import io.github.theapache64.perfboy.di.module.RepoModule
import io.github.theapache64.perfboy.di.module.TraceParserModule
import io.github.theapache64.perfboy.ui.home.AnalyzeActivity
import io.github.theapache64.perfboy.ui.splash.SplashActivity
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


