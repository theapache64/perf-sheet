package io.github.theapache64.perfsheet.data.repo

import io.github.theapache64.perfsheet.model.FinalResult
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import javax.inject.Inject

interface ExcelRepo {
    fun make(
        xlsFile: File,
        allThreadData: Map<String, FinalResult>,
        mainThreadData: Map<String, FinalResult>,
        backgroundThreadData: Map<String, FinalResult>,
        onProgress: (String) -> Unit,
    )
}


class ExcelRepoImpl @Inject constructor() : ExcelRepo {

    enum class SheetTypes(val title: String) {
        ALL_THREADS("All Threads"),
        MAIN_THREAD("Main Thread"),
        BACKGROUND_THREADS("Background Threads")
    }

    enum class Heading(val title: String) {
        METHOD_NAME("Method Name"),
        BEFORE_MS("Before (ms)"),
        AFTER_MS("After (ms)"),
        DIFF("Diff (ms)"),
        COUNT_DIFF("Count diff"),
        BEFORE_THREAD("Before summary"),
        AFTER_THREAD("After summary");
    }

    override fun make(
        xlsFile: File,
        allThreadData: Map<String, FinalResult>,
        mainThreadData: Map<String, FinalResult>,
        backgroundThreadData: Map<String, FinalResult>,
        onProgress: (String) -> Unit,
    ) {
        val sheetMap = mapOf<SheetTypes, Map<String, FinalResult>>(
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
                        Heading.BEFORE_MS -> 13
                        Heading.AFTER_MS -> 13
                        Heading.DIFF -> 12
                        Heading.COUNT_DIFF -> 18
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
                row.createCell(1).setCellValue(result.beforeDurationInMs.toDouble())
                row.createCell(2).setCellValue(result.afterDurationInMs.toDouble())
                row.createCell(3).setCellValue(result.diffInMs.toDouble())
                row.createCell(4).setCellValue(result.countComparison)
                row.createCell(5).setCellValue(result.beforeComparison)
                row.createCell(6).setCellValue(result.afterComparison)
            }
        }

        onProgress("🚀 Writing to file: ${xlsFile.name}")
        workbook.write(xlsFile.outputStream())
        workbook.close()
    }


}

