package io.github.theapache64.perfsheet.data.repo

import io.github.theapache64.perfsheet.model.ResultRow
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import javax.inject.Inject

interface ExcelRepo {
    fun make(
        xlsFile: File,
        allThreadData: Map<String, ResultRow>,
        mainThreadData: Map<String, ResultRow>,
        backgroundThreadData: Map<String, ResultRow>,
        allThreadDataMinified: Map<String, ResultRow>,
        mainThreadMinified: Map<String, ResultRow>,
        onProgress: (String) -> Unit,
    )
}


class ExcelRepoImpl @Inject constructor() : ExcelRepo {

    enum class SheetTypes(val title: String) {
        ALL_THREADS("All Threads"),
        MAIN_THREAD("Main Thread"),
        BACKGROUND_THREADS("Background Threads"),
        ALL_THREADS_MINIFIED("All Threads (minified)"),
        MAIN_THREAD_MINIFIED("Main Thread (minified)"),
    }

    enum class Heading(
        val title: String,
        val colWidth : Int
    ) {
        METHOD_NAME("Method Name", 60),
        BEFORE_MS("Before (ms)", 13),
        AFTER_MS("After (ms)", 13),
        DIFF("Diff (ms)", 12),
        BEFORE_COUNT("Before count", 13),
        AFTER_COUNT("After count", 13),
        COUNT_DIFF("Count diff", 18),
        BEFORE_THREAD("Before summary", 60),
        AFTER_THREAD("After summary", 60);
    }

    override fun make(
        xlsFile: File,
        allThreadData: Map<String, ResultRow>,
        mainThreadData: Map<String, ResultRow>,
        backgroundThreadData: Map<String, ResultRow>,
        allThreadDataMinified: Map<String, ResultRow>,
        mainThreadMinified: Map<String, ResultRow>,
        onProgress: (String) -> Unit,
    ) {
        xlsFile.delete()
        val sheetMap = mapOf(
            SheetTypes.ALL_THREADS to allThreadData,
            SheetTypes.MAIN_THREAD to mainThreadData,
            SheetTypes.BACKGROUND_THREADS to backgroundThreadData,
            SheetTypes.ALL_THREADS_MINIFIED to allThreadDataMinified,
            SheetTypes.MAIN_THREAD_MINIFIED to mainThreadMinified,

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
                    fontHeightInPoints = 12
                })
            }

            val headerRow = sheet.createRow(0)

            // Heading
            for ((index, heading) in Heading.entries.withIndex()) {
                headerRow.createCell(index).apply {
                    setCellValue(heading.title)
                    cellStyle = headerStyle
                    sheet.setColumnWidth(index, heading.colWidth * 256)
                }
            }

            onProgress("📝 Writing data to sheet: ${sheetType.title}")

            // Freeze first row
            sheet.createFreezePane(0, 1)

            // Data
            for ((methodName, result) in sheetData) {
                val row = sheet.createRow(sheet.lastRowNum + 1)
                // set row height to 22pt
                row.heightInPoints = 22f

                row.createCell(0).setCellValue(methodName)
                row.createCell(1).setCellValue(result.beforeDurationInMs.toDouble())
                row.createCell(2).setCellValue(result.afterDurationInMs.toDouble())
                row.createCell(3).setCellValue(result.diffInMs.toDouble())
                row.createCell(4).setCellValue(result.beforeCount.toDouble())
                row.createCell(5).setCellValue(result.afterCount.toDouble())
                row.createCell(6).setCellValue(result.countComparison.toDouble())
                row.createCell(7).setCellValue(result.beforeComparison)
                row.createCell(8).setCellValue(result.afterComparison)
            }
        }

        onProgress("🚀 Writing to file: ${xlsFile.name}")
        workbook.write(xlsFile.outputStream())
        workbook.close()
    }
}


