package io.github.theapache64.perfboy.app

import io.github.theapache64.perfboy.data.repo.AppRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppController @Inject constructor(
    private val appRepo: AppRepo,
) {
    fun onArgs(args: Array<String>?) {
        appRepo.args = args?.toList()
    }
}