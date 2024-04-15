package io.github.theapache64.perfsheet.app

import com.theapache64.cyclone.core.Application
import io.github.theapache64.perfsheet.di.AppComponent
import io.github.theapache64.perfsheet.di.DaggerAppComponent
import io.github.theapache64.perfsheet.ui.splash.SplashActivity
import javax.inject.Inject


/**
 * Application class
 */
class App : Application() {

    companion object {
        var args: Array<String>? = null
        lateinit var di: AppComponent
    }

    @Inject
    lateinit var appController: AppController

    override fun onCreate() {
        super.onCreate()
        di = DaggerAppComponent.create()
        di.inject(this)

        appController.onArgs(args)

        val splashIntent = SplashActivity.getStartIntent()
        startActivity(splashIntent)
    }
}

/**
 * Entry point
 */
fun main(args: Array<String>) {
    App.args = args
    App().onCreate()
}