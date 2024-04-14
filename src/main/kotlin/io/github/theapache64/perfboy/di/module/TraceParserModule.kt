package io.github.theapache64.perfboy.di.module

import dagger.Module
import dagger.Provides
import io.github.theapache64.perfboy.core.TraceLogger
import io.github.theapache64.perfboy.traceparser.analyzer.TraceAnalyzer
import io.github.theapache64.perfboy.traceparser.core.AppLogger
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