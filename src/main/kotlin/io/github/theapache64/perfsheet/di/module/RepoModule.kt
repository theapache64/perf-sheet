package io.github.theapache64.perfsheet.di.module

import dagger.Binds
import dagger.Module
import io.github.theapache64.perfsheet.data.repo.AppRepo
import io.github.theapache64.perfsheet.data.repo.AppRepoImpl
import io.github.theapache64.perfsheet.data.repo.ExcelRepo
import io.github.theapache64.perfsheet.data.repo.ExcelRepoImpl
import io.github.theapache64.perfsheet.data.repo.TraceRepo
import io.github.theapache64.perfsheet.data.repo.TraceRepoImpl

@Module
abstract class RepoModule {
    @Binds
    abstract fun bindAppRepo(appRepo: AppRepoImpl): AppRepo

    @Binds
    abstract fun bindTraceRepo(traceRepo: TraceRepoImpl): TraceRepo

    @Binds
    abstract fun bindExcelRepo(excelRepo: ExcelRepoImpl): ExcelRepo
}