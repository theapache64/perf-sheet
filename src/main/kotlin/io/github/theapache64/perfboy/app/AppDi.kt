package io.github.theapache64.perfboy.app

import io.github.theapache64.perfboy.di.module.RepoModule
import io.github.theapache64.perfboy.ui.splash.SplashActivity
import dagger.Component
import io.github.theapache64.perfboy.ui.home.AnalyzeActivity
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        RepoModule::class
    ]
)
interface AppComponent {
    fun inject(app: App)
    fun inject(splashActivity: SplashActivity)
    fun inject(analyzeActivity: AnalyzeActivity)
}


