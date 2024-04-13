package io.github.theapache64.perfboy.di.module

import io.github.theapache64.perfboy.data.repo.AppRepo
import io.github.theapache64.perfboy.data.repo.AppRepoImpl
import dagger.Binds
import dagger.Module

@Module
abstract class RepoModule {

    @Binds
    abstract fun bindAppRepo(appRepo: AppRepoImpl) : AppRepo
}