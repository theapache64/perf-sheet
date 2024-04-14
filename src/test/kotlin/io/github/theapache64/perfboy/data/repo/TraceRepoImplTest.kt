package io.github.theapache64.perfboy.data.repo

import io.github.theapache64.perfboy.DaggerTestAppComponent
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import java.io.File
import javax.inject.Inject

class TraceRepoImplTest {


    @Inject
    lateinit var traceRepoImpl: TraceRepoImpl

    @BeforeEach
    fun before(){
        DaggerTestAppComponent.create().inject(this)
    }

    @Test
    fun parseTest() {
        val result = traceRepoImpl.parse(
            // TODO: Move these files to src/test/resources
            beforeTrace = File("/Users/theapache64/Desktop/perf-boy/before.trace"),
            afterTrace = File("/Users/theapache64/Desktop/perf-boy/after.trace")
        )

        assert(result.isNotEmpty())

    }
}