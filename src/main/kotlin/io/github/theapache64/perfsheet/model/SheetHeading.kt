package io.github.theapache64.perfsheet.model

data class Heading(
    val title: String,
    val colWidth: Int
)

val dualTraceHeadings = listOf(
    Heading("Method Name", 60),
    Heading("Before (ms)", 13),
    Heading("After (ms)", 13),
    Heading("Diff (ms)", 12),
    Heading("Before count", 13),
    Heading("After count", 13),
    Heading("Count diff", 18),
    Heading("Before summary", 60),
    Heading("After summary", 60)
)

val singleTraceHeadings = listOf(
    Heading("Method Name", 60),
    Heading("Duration (ms)", 13),
    Heading("Count", 13),
    Heading("Summary", 60)
)

val dualFrameHeadings = listOf(
    Heading("Frame#", 60),
    Heading("Before (ms)", 13),
    Heading("After (ms)", 13),
    Heading("Diff (ms)", 13)

)

val singleFrameHeadings = listOf(
    Heading("Frame#", 60 ),
    Heading("Duration (ms)", 13),
)