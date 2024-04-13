package io.github.theapache64.perfboy.app

import io.github.theapache64.perfboy.di.module.RepoModule
import io.github.theapache64.perfboy.ui.splash.SplashActivity
import dagger.Component
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
}


