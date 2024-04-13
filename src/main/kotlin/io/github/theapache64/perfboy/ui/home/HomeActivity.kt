package io.github.theapache64.perfboy.ui.home

import com.theapache64.cyclone.core.Activity
import com.theapache64.cyclone.core.Intent
import io.github.theapache64.perfboy.app.App
import io.github.theapache64.perfboy.ui.splash.SplashViewModel
import javax.inject.Inject

class HomeActivity : Activity() {
    companion object {
        fun getStartIntent(): Intent {
            return Intent(HomeActivity::class)
        }
    }

    @Inject
    lateinit var viewModel: HomeViewModel

    override fun onCreate() {
        super.onCreate()
        App.di.inject(this)

        viewModel.statusMsg.observe { statusMsg ->
            println(statusMsg)
        }
    }
}