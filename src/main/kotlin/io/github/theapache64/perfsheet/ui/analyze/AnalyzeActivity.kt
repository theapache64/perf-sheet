package io.github.theapache64.perfsheet.ui.analyze

import com.theapache64.cyclone.core.Activity
import com.theapache64.cyclone.core.Intent
import io.github.theapache64.perfsheet.app.App
import javax.inject.Inject

class AnalyzeActivity : Activity() {
    companion object {
        fun getStartIntent(): Intent {
            return Intent(AnalyzeActivity::class)
        }
    }

    @Inject
    lateinit var viewModel: AnalyzeViewModel

    override fun onCreate() {
        super.onCreate()
        App.di.inject(this)

        viewModel.statusMsg.observe { statusMsg ->
            println(statusMsg)
        }

        viewModel.init()
    }
}