package io.github.theapache64.perfboy.data.repo

import io.github.theapache64.perfboy.data.local.TraceResult
import io.github.theapache64.perfboy.data.repo.Heading.AFTER_MS
import io.github.theapache64.perfboy.data.repo.Heading.AFTER_THREAD
import io.github.theapache64.perfboy.data.repo.Heading.BEFORE_MS
import io.github.theapache64.perfboy.data.repo.Heading.BEFORE_THREAD
import io.github.theapache64.perfboy.data.repo.Heading.COUNT_DIFF
import io.github.theapache64.perfboy.data.repo.Heading.DIFF
import io.github.theapache64.perfboy.data.repo.Heading.METHOD_NAME
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import kotlin.math.roundToLong

interface ExcelRepo {

}

enum class Heading(val title: String) {
    METHOD_NAME("Method Name"),
    BEFORE_MS("Before (ms)"),
    AFTER_MS("After (ms)"),
    DIFF("Diff"),
    COUNT_DIFF("Count diff"),
    BEFORE_THREAD("Before Thread"),
    AFTER_THREAD("After Thread");
}

enum class SheetType {
    ALL_THREADS,
    MAIN_THREAD,
    BACKGROUND_THREADS
}

class Sheet(
    prisheetType: SheetType,
    sheet : XSSFSheet
)

class ExcelRepoImpl(

) : ExcelRepo {

    fun write(
        xlsFile: File,
        data: Map<String, TraceResult>
    ) {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("All Threads")

        // Creating style for black background and white font color with bigger font size
        val headerStyle = workbook.createCellStyle().apply {
            fillForegroundColor = org.apache.poi.ss.usermodel.IndexedColors.BLACK.index
            fillPattern = org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND
            setFont(workbook.createFont().apply {
                color = org.apache.poi.ss.usermodel.IndexedColors.WHITE.index
                bold = true
                fontHeightInPoints = 12
            })
        }

        val headerRow = sheet.createRow(0)

        // Heading
        for ((index, heading) in Heading.entries.withIndex()) {
            headerRow.createCell(index).apply {
                setCellValue(heading.title)
                cellStyle = headerStyle
                val width = when (heading) {
                    METHOD_NAME -> 60
                    BEFORE_MS -> 10
                    AFTER_MS -> 10
                    DIFF -> 10
                    COUNT_DIFF -> 16
                    BEFORE_THREAD -> 60
                    AFTER_THREAD -> 60
                }

                sheet.setColumnWidth(index, width * 256)
            }
        }

        // Freeze first row
        sheet.createFreezePane(0, 1)

        // Data
        for ((methodName, result) in data) {
            val row = sheet.createRow(sheet.lastRowNum + 1)
            row.createCell(0).setCellValue(methodName)
            row.createCell(1).setCellValue(result.beforeDurationInMs.toLong().hyphenIfMinusOne())
            row.createCell(2).setCellValue(result.afterDurationInMs.toLong().hyphenIfMinusOne())
            row.createCell(3).setCellValue(result.diffInMs.roundToLong().toString())
            row.createCell(4).setCellValue(
                """
                    Before: ${result.beforeCount.takeIf { it > 1 } ?: "not present"}
                    After: ${result.afterCount.takeIf { it > 1 } ?: "not present"}
                    
                    ${result.countLabel}
                """.trimIndent()
            )
            row.createCell(5).setCellValue(
                result.beforeThreadDetails.toReadableString()
            )
            row.createCell(6).setCellValue(
                result.afterThreadDetails.toReadableString(result.beforeThreadDetails)
            )
        }

        workbook.write(xlsFile.outputStream())
        workbook.close()
    }
}

private fun List<TraceResult.ThreadDetail>.toReadableString(
    beforeThreadDetails: List<TraceResult.ThreadDetail>? = null
): String {

    return joinToString(separator = "\n\n") {
        """
            🧵 ${it.threadName}, ⏱️${it.totalDurationInMs.roundToLong()}ms, ⏹︎ (${it.noOfBlocks} block[s])
        """.trimIndent()
    }
}

private fun Long.hyphenIfMinusOne(): String? {
    if (this == -1L) return "not present"
    return this.toString()
}
