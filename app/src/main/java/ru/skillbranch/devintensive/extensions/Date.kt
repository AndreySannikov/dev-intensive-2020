package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*

const val SECOND: Long = 1000
const val MINUTE: Long = 60 * SECOND
const val HOUR: Long = 60 * MINUTE
const val DAY: Long = 24 * HOUR

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String =
    SimpleDateFormat(pattern, Locale("ru")).format(this)

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND) = this.also {
    it.time += value * when (units) {
        TimeUnits.DAY -> DAY
        TimeUnits.HOUR -> HOUR
        TimeUnits.MINUTE -> MINUTE
        TimeUnits.SECOND -> SECOND
    }
}

fun Date.humanizeDiff(that: Date = Date()) = (time - that.time).let {
    when {
        it < -360 * DAY     -> "более года назад"
        it < -26 * HOUR     -> "${ TimeUnits.DAY.plural((-it / DAY).toInt()) } назад"
        it < -22 * HOUR     -> "день назад"
        it < -75 * MINUTE   -> "${ TimeUnits.HOUR.plural((-it / HOUR).toInt()) } назад"
        it < -45 * MINUTE   -> "час назад"
        it < -75 * SECOND   -> "${ TimeUnits.MINUTE.plural((-it / MINUTE).toInt()) } назад"
        it < -45 * SECOND   -> "минуту назад"
        it < -1 * SECOND    -> "несколько секунд назад"

        it <= 1 * SECOND    -> "только что"

        it <= 45 * SECOND   -> "через несколько секунд"
        it <= 75 * SECOND   -> "через минуту"
        it <= 45 * MINUTE   -> "через ${ TimeUnits.MINUTE.plural((it / MINUTE).toInt()) }"
        it <= 75 * MINUTE   -> "через час"
        it <= 22 * HOUR     -> "через ${ TimeUnits.HOUR.plural((it / HOUR).toInt()) }"
        it <= 26 * HOUR     -> "через день"
        it <= 360 * DAY     -> "через ${ TimeUnits.DAY.plural((it / DAY).toInt()) }"
        else                -> "более чем через год"
    }
}

enum class TimeUnits(
    private val single: String,
    private val several: String,
    private val many: String
) {
    SECOND("секунду", "секунды", "секунд"),
    MINUTE("минуту", "минуты", "минут"),
    HOUR("час", "часа", "часов"),
    DAY("день", "дня", "дней");

    fun plural(amount: Int) = when (amount) {
        1 -> "1 $single"
        in 2..4 -> "$amount $several"
        else -> "$amount $many"
    }
}