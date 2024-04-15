package io.github.theapache64.perfsheet.di.module

import dagger.Module
import dagger.Provides
import io.github.theapache64.perfsheet.core.TraceLogger
import io.github.theapache64.perfsheet.traceparser.analyzer.TraceAnalyzer
import io.github.theapache64.perfsheet.traceparser.core.AppLogger
import javax.inject.Singleton

@Module
class TraceParserModule {

    @Singleton
    @Provides
    fun provideAppLogger(): AppLogger {
        return TraceLogger(isDebug = false)
    }

    @Singleton
    @Provides
    fun provideTraceAnalyzer(
        appLogger: AppLogger
    ): TraceAnalyzer {
        return TraceAnalyzer(appLogger)
    }
}