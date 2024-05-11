package io.github.theapache64.perfsheet.data.repo

import io.github.theapache64.perfsheet.data.repo.ExcelRepoImpl.SheetTypes.ALL_THREADS
import io.github.theapache64.perfsheet.data.repo.ExcelRepoImpl.SheetTypes.ALL_THREADS_MINIFIED
import io.github.theapache64.perfsheet.data.repo.ExcelRepoImpl.SheetTypes.BACKGROUND_THREADS
import io.github.theapache64.perfsheet.data.repo.ExcelRepoImpl.SheetTypes.FRAMES
import io.github.theapache64.perfsheet.data.repo.ExcelRepoImpl.SheetTypes.MAIN_THREAD
import io.github.theapache64.perfsheet.data.repo.ExcelRepoImpl.SheetTypes.MAIN_THREAD_MINIFIED
import io.github.theapache64.perfsheet.model.Heading
import io.github.theapache64.perfsheet.model.ResultRow
import io.github.theapache64.perfsheet.model.dualFrameHeadings
import io.github.theapache64.perfsheet.model.dualTraceHeadings
import io.github.theapache64.perfsheet.model.singleFrameHeadings
import io.github.theapache64.perfsheet.model.singleTraceHeadings
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import javax.inject.Inject

interface ExcelRepo {
    fun make(
        xlsFile: File,
        isSingle: Boolean,
        allThreadData: Map<String, ResultRow>,
        mainThreadData: Map<String, ResultRow>,
        backgroundThreadData: Map<String, ResultRow>,
        allThreadDataMinified: Map<String, ResultRow>,
        mainThreadMinified: Map<String, ResultRow>,
        frames: Map<String, ResultRow>,
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
        FRAMES("Frames")
    }


    override fun make(
        xlsFile: File,
        isSingle: Boolean,
        allThreadData: Map<String, ResultRow>,
        mainThreadData: Map<String, ResultRow>,
        backgroundThreadData: Map<String, ResultRow>,
        allThreadDataMinified: Map<String, ResultRow>,
        mainThreadMinified: Map<String, ResultRow>,
        frames: Map<String, ResultRow>,
        onProgress: (String) -> Unit
    ) {
        xlsFile.delete()
        val sheetMap = mapOf(
            ALL_THREADS to allThreadData,
            MAIN_THREAD to mainThreadData,
            BACKGROUND_THREADS to backgroundThreadData,
            ALL_THREADS_MINIFIED to allThreadDataMinified,
            MAIN_THREAD_MINIFIED to mainThreadMinified,
            FRAMES to frames
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
            when (sheetType) {
                ALL_THREADS,
                MAIN_THREAD,
                BACKGROUND_THREADS,
                ALL_THREADS_MINIFIED,
                MAIN_THREAD_MINIFIED -> {
                    if (isSingle) {
                        renderHeading(headerRow, headerStyle, sheet, singleTraceHeadings)
                    } else {
                        renderHeading(headerRow, headerStyle, sheet, dualTraceHeadings)
                    }
                }

                FRAMES -> {
                    if (isSingle) {
                        renderHeading(headerRow, headerStyle, sheet, singleFrameHeadings)
                    } else {
                        renderHeading(headerRow, headerStyle, sheet, dualFrameHeadings)
                    }
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
                when (result) {
                    is ResultRow.DualTrace -> {
                        row.createCell(1).setCellValue(result.beforeDurationInMs.toDouble())
                        row.createCell(2).setCellValue(result.afterDurationInMs.toDouble())
                        row.createCell(3).setCellValue(result.diffInMs.toDouble())
                        row.createCell(4).setCellValue(result.beforeCount.toDouble())
                        row.createCell(5).setCellValue(result.afterCount.toDouble())
                        row.createCell(6).setCellValue(result.countComparison.toDouble())
                        row.createCell(7).setCellValue(result.beforeComparison)
                        row.createCell(8).setCellValue(result.afterComparison)
                    }

                    is ResultRow.SingleTrace -> {
                        row.createCell(1).setCellValue(result.durationInMs.toDouble())
                        row.createCell(2).setCellValue(result.count.toDouble())
                        row.createCell(3).setCellValue(result.comparison)
                    }

                    is ResultRow.DualFrame -> {
                        row.createCell(1).setCellValue(result.beforeDurationInMs.toDouble())
                        row.createCell(2).setCellValue(result.afterDurationInMs.toDouble())
                        row.createCell(3).setCellValue(result.diffInMs.toDouble())
                    }
                    is ResultRow.SingleFrame -> {
                        row.createCell(1).setCellValue(result.durationInMs.toDouble())
                    }
                }
            }
        }

        onProgress("🚀 Writing to file: ${xlsFile.name}")
        workbook.write(xlsFile.outputStream())
        workbook.close()
    }

    private fun renderHeading(
        headerRow: XSSFRow,
        headerStyle: XSSFCellStyle?,
        sheet: XSSFSheet,
        headings: List<Heading>
    ) {
        for ((index, heading) in headings.withIndex()) {
            createHeading(headerRow, index, heading.title, heading.colWidth, headerStyle, sheet)
        }
    }

    private fun createHeading(
        headerRow: XSSFRow,
        index: Int,
        title: String,
        colWidth: Int,
        headerStyle: XSSFCellStyle?,
        sheet: XSSFSheet
    ) {
        headerRow.createCell(index).apply {
            setCellValue(title)
            cellStyle = headerStyle
            sheet.setColumnWidth(index, colWidth * 256)
        }
    }
}


