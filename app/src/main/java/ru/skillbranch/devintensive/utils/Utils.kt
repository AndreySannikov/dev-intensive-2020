package ru.skillbranch.devintensive.utils

object Utils {

    fun parseFullName(fullName: String?) = (fullName?.split(" ") ?: listOf(null, null)).let {
        Pair(
            it.getOrNull(0)?.ifEmpty { null },
            it.getOrNull(1)?.ifEmpty { null }
        )
    }

    fun toInitials(firstName: String?, lastName: String?) =
        (firstUpOrEmpty(firstName) + firstUpOrEmpty(lastName)).ifEmpty { null }

    private fun firstUpOrEmpty(str: String?) = str?.trim()?.let {
        if (it.isNotEmpty()) it.substring(0, 1).toUpperCase() else ""
    }.orEmpty()

    fun transliteration(str: String, separator: String = " ") = str.split(" ").joinToString(separator) {
        it.map { char ->
            when (char.toLowerCase().toString()) {
                "а" -> "a"
                "б" -> "b"
                "в" -> "v"
                "г" -> "g"
                "д" -> "d"
                "е" -> "e"
                "ё" -> "e"
                "ж" -> "zh"
                "з" -> "z"
                "и" -> "i"
                "й" -> "i"
                "к" -> "k"
                "л" -> "l"
                "м" -> "m"
                "н" -> "n"
                "о" -> "o"
                "п" -> "p"
                "р" -> "r"
                "с" -> "s"
                "т" -> "t"
                "у" -> "u"
                "ф" -> "f"
                "х" -> "h"
                "ц" -> "c"
                "ч" -> "ch"
                "ш" -> "sh"
                "щ" -> "sh'"
                "ъ" -> ""
                "ы" -> "i"
                "ь" -> ""
                "э" -> "e"
                "ю" -> "yu"
                "я" -> "ya"
                else -> char.toString()
            }.run { if (char.isUpperCase()) this.capitalize() else this }
        }.joinToString(separator = "")
    }
}