package ru.skillbranch.devintensive.extensions

fun String.truncate(limit: Int = 16) = trimEnd().let {
    when {
        it.length > limit   -> it.substring(0, limit) + "..."
        else                -> it
    }
}

fun String.stripHtml() =
    htmlTagRegex.replace(this, "")
        .run { escapeHtmlRegex.replace(this, "") }
        .run { multipleSpaces.replace(this, " ") }

private val htmlTagRegex = Regex("<[^>]+>")
private val escapeHtmlRegex = Regex("&[^;]+;")
private val multipleSpaces = Regex("\\s{2,}")