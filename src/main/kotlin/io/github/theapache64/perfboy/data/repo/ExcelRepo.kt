package io.github.theapache64.perfboy.data.repo

import io.github.theapache64.perfboy.data.local.TraceResult
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import javax.inject.Inject
import kotlin.math.roundToLong

interface ExcelRepo {
    fun make(
        xlsFile: File,
        allThreadData: Map<String, TraceResult>,
        mainThreadData: Map<String, TraceResult>,
        backgroundThreadData: Map<String, TraceResult>,
        onProgress: (String) -> Unit,
    )
}


class ExcelRepoImpl @Inject constructor() : ExcelRepo {

    companion object{
        private const val NOT_PRESENT = "not present"
    }

    enum class SheetTypes(val title: String) {
        ALL_THREADS("All Threads"),
        MAIN_THREAD("Main Thread"),
        BACKGROUND_THREADS("Background Threads")
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

    override fun make(
        xlsFile: File,
        allThreadData: Map<String, TraceResult>,
        mainThreadData: Map<String, TraceResult>,
        backgroundThreadData: Map<String, TraceResult>,
        onProgress: (String) -> Unit,
    ) {
        val sheetMap = mapOf<SheetTypes, Map<String, TraceResult>>(
            SheetTypes.ALL_THREADS to allThreadData,
            SheetTypes.MAIN_THREAD to mainThreadData,
            SheetTypes.BACKGROUND_THREADS to backgroundThreadData
        )


        val workbook = XSSFWorkbook()

        for ((sheetType, sheetData) in sheetMap) {
            val sheet = workbook.createSheet(sheetType.title)

            onProgress("📜 Creating sheet: ${sheetType.title}")

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
                        Heading.METHOD_NAME -> 60
                        Heading.BEFORE_MS -> 10
                        Heading.AFTER_MS -> 10
                        Heading.DIFF -> 10
                        Heading.COUNT_DIFF -> 16
                        Heading.BEFORE_THREAD -> 60
                        Heading.AFTER_THREAD -> 60
                    }

                    sheet.setColumnWidth(index, width * 256)
                }
            }

            onProgress("📝 Writing data to sheet: ${sheetType.title}")

            // Freeze first row
            sheet.createFreezePane(0, 1)

            // Data
            for ((methodName, result) in sheetData) {
                val row = sheet.createRow(sheet.lastRowNum + 1)
                row.createCell(0).setCellValue(methodName)
                row.createCell(1).setCellValue(result.beforeDurationInMs.toLong().notPresentIfMinusOne())
                row.createCell(2).setCellValue(result.afterDurationInMs.toLong().notPresentIfMinusOne())
                row.createCell(3).setCellValue(result.diffInMs.roundToLong().toString())

                row.createCell(4).setCellValue(
                    """
                    Before: ${result.beforeCount.takeIf { it >= 1 } ?: NOT_PRESENT}
                    After: ${result.afterCount.takeIf { it >= 1 } ?: NOT_PRESENT}
                    
                    ${result.countLabel}
                """.trimIndent()
                )
                row.createCell(5).setCellValue(
                    summarise(
                        sheetType = sheetType,
                        before = result.beforeThreadDetails,
                        compareWith = null
                    ).ifBlank { NOT_PRESENT }
                )
                row.createCell(6).setCellValue(
                    summarise(
                        sheetType = sheetType,
                        before = result.afterThreadDetails,
                        compareWith = result.beforeThreadDetails
                    ).ifBlank { NOT_PRESENT }
                )
            }
        }

        onProgress("🚀 Writing to file: ${xlsFile.name}")
        workbook.write(xlsFile.outputStream())
        workbook.close()
    }


    private fun summarise(
        sheetType: SheetTypes,
        before: List<TraceResult.ThreadDetail>,
        compareWith: List<TraceResult.ThreadDetail>?
    ): String {

        return before.joinToString(separator = "\n") { afterThread ->
            val threadName = if (sheetType != SheetTypes.MAIN_THREAD) {
                "🧵 ${afterThread.threadName}, "
            } else {
                ""
            }
            val summary =
                "${threadName}⏱️${afterThread.totalDurationInMs.roundToLong()}ms, ⏹︎ (${afterThread.noOfBlocks} ${if (afterThread.noOfBlocks > 1) "blocks" else "block"})"
            if (compareWith == null) {
                summary
            } else {
                val beforeDuration =
                    compareWith.find { beforeThread -> beforeThread.threadName == afterThread.threadName }?.totalDurationInMs?.roundToLong()
                        ?: 0
                val afterDuration = afterThread.totalDurationInMs.roundToLong()
                val durationDiff = afterDuration - beforeDuration
                // if negative '-' else +, if zero nothing
                val sign = if (durationDiff > 0) "+" else ""

                val beforeBlocks =
                    compareWith.find { beforeThread -> beforeThread.threadName == afterThread.threadName }?.noOfBlocks
                        ?: 0
                val afterBlocks = afterThread.noOfBlocks
                val blocksDiff = afterBlocks - beforeBlocks
                val blockSign = if (blocksDiff > 0) "+" else ""

                val comparison = "Change: $sign${durationDiff}ms, $blockSign${blocksDiff} blocks"

                """
                $summary
                $comparison
            """.trimIndent()
            }
        }
    }

    private fun Long.notPresentIfMinusOne(): String? {
        if (this == -1L) return NOT_PRESENT
        return this.toString()
    }
}

