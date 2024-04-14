package io.github.theapache64.perfboy.di.module

import dagger.Binds
import dagger.Module
import io.github.theapache64.perfboy.data.repo.AppRepo
import io.github.theapache64.perfboy.data.repo.AppRepoImpl
import io.github.theapache64.perfboy.data.repo.ExcelRepo
import io.github.theapache64.perfboy.data.repo.ExcelRepoImpl
import io.github.theapache64.perfboy.data.repo.TraceRepo
import io.github.theapache64.perfboy.data.repo.TraceRepoImpl

@Module
abstract class RepoModule {
    @Binds
    abstract fun bindAppRepo(appRepo: AppRepoImpl): AppRepo

    @Binds
    abstract fun bindTraceRepo(traceRepo: TraceRepoImpl): TraceRepo

    @Binds
    abstract fun bindExcelRepo(excelRepo: ExcelRepoImpl): ExcelRepo
}