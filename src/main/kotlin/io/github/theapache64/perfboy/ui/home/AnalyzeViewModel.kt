package io.github.theapache64.perfboy.ui.home

import com.theapache64.cyclone.core.livedata.LiveData
import com.theapache64.cyclone.core.livedata.MutableLiveData
import io.github.theapache64.perfboy.data.repo.AppRepo
import javax.inject.Inject

class AnalyzeViewModel @Inject constructor(
    appRepo: AppRepo
) {
    enum class Mode {
        ANALYZE,
        ANALYZE_AND_COMPARE
    }

    private val _statusMsg = MutableLiveData<String>()
    val statusMsg: LiveData<String> = _statusMsg

    init {
        val mode = when (appRepo.args?.size) {
            1 -> Mode.ANALYZE
            2 -> Mode.ANALYZE_AND_COMPARE
            else -> throw IllegalStateException("Can't accept more than 2 params")
        }

        if (mode == Mode.ANALYZE) {
            TODO("Single trace analysis mode not implemented yet. Please vote")
        }
    }
}