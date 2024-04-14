package io.github.theapache64.perfboy.data.repo

import de.siegmar.fastcsv.writer.CsvWriter
import io.github.theapache64.perfboy.data.local.TraceResult
import java.io.File
import java.io.StringWriter
import kotlin.math.roundToLong

interface CsvRepo {

}

class CsvRepoImpl(

) : CsvRepo {
    fun write(
        csvFile: File,
        data: Map<String, TraceResult>
    ) {
        val csvStringWriter = StringWriter()
        val writer = CsvWriter.builder().build(csvStringWriter)

        // heading
        writer.writeRecord(
            "Method Name", "Before (ms)", "After (ms)", "Diff", "Count diff", "Before Thread", "After Thread"
        )

        // Values
        for ((methodName, result) in data) {
            writer.writeRecord(
                methodName,
                result.beforeDurationInMs.toLong().hyphenIfMinusOne(),
                result.afterDurationInMs.toLong().hyphenIfMinusOne(),
                result.diffInMs.roundToLong().toString(),
                """
                    Before: ${result.beforeCount.takeIf { it > 1 } ?: "-"}
                    After: ${result.afterCount.takeIf { it > 1 } ?: "-"}
                    
                    ${result.countLabel}
                """.trimIndent(),
                result.beforeThreadDetails.toReadableString(),
                result.afterThreadDetails.toReadableString(result.beforeThreadDetails),
            )
        }

        csvFile.writeText(csvStringWriter.toString())
    }
}

private fun List<TraceResult.ThreadDetail>.toReadableString(
    beforeThreadDetails : List<TraceResult.ThreadDetail>? = null
): String {

    return joinToString(separator = "\n\n") {
        """
            🧵 ${it.threadName}, ⏱️${it.totalDurationInMs.roundToLong()}ms, ⏹︎ (${it.noOfBlocks} block[s])
        """.trimIndent()
    }
}

private fun Long.hyphenIfMinusOne(): String? {
    if (this == -1L) return "-"
    return this.toString()
}
