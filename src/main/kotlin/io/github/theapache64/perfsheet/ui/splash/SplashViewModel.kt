package io.github.theapache64.perfsheet.ui.splash

import com.theapache64.cyclone.core.livedata.LiveData
import com.theapache64.cyclone.core.livedata.MutableLiveData
import javax.inject.Inject

class SplashViewModel @Inject constructor() {
    companion object {
        private const val VERSION = "0.0.5"
    }

    private val _welcomeMsg = MutableLiveData<String>()
    val welcomeMsg: LiveData<String> = _welcomeMsg

    private val _goToHome = MutableLiveData<String>()
    val goToHome: LiveData<String> = _goToHome

    init {
        val splashMsg = "perf-sheet ($VERSION)"
        _welcomeMsg.value = splashMsg
        _goToHome.value = splashMsg
    }


}
