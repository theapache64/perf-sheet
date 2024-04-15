package io.github.theapache64.perfsheet.model

data class Method(
    var name: String,
    val nodes: MutableList<Node>,
)

