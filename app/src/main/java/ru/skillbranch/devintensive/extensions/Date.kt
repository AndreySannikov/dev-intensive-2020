package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*

const val SECOND: Long = 1000
const val MINUTE: Long = 60 * SECOND
const val HOUR: Long = 60 * MINUTE
const val DAY: Long = 24 * HOUR

fun Date.plural(pattern: String = "HH:mm:ss dd.MM.yy"): String =
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
        it < -26 * HOUR     -> "${ TimeUnits.DAY.plural(-it / DAY) } назад"
        it < -22 * HOUR     -> "день назад"
        it < -75 * MINUTE   -> "${ TimeUnits.HOUR.plural(-it / HOUR) } назад"
        it < -45 * MINUTE   -> "час назад"
        it < -75 * SECOND   -> "${ TimeUnits.MINUTE.plural(-it / MINUTE) } назад"
        it < -45 * SECOND   -> "минуту назад"
        it < -1 * SECOND    -> "несколько секунд назад"

        it <= 1 * SECOND    -> "только что"

        it <= 45 * SECOND   -> "через несколько секунд"
        it <= 75 * SECOND   -> "через минуту"
        it <= 45 * MINUTE   -> "через ${ TimeUnits.MINUTE.plural(it / MINUTE) }"
        it <= 75 * MINUTE   -> "через час"
        it <= 22 * HOUR     -> "через ${ TimeUnits.HOUR.plural(it / HOUR) }"
        it <= 26 * HOUR     -> "через день"
        it <= 360 * DAY     -> "через ${ TimeUnits.DAY.plural(it / DAY) }"
        else                -> "более чем через год"
    }

}

enum class TimeUnits {
    SECOND {
        override fun plural(amount: Long) = when {
            amount == 1L -> "1 секунду"
            amount < 5  -> "$amount секунды"
            else        -> "$amount секунд"
        }
    },
    MINUTE {
        override fun plural(amount: Long) = when {
            amount == 1L -> "1 минуту"
            amount < 5  -> "$amount минуты"
            else        -> "$amount минут"
        }
    },
    HOUR {
        override fun plural(amount: Long) = when {
            amount == 1L -> "1 час"
            amount < 5  -> "$amount часа"
            else        -> "$amount часов"
        }
    },
    DAY {
        override fun plural(amount: Long) = when {
            amount == 1L -> "1 день"
            amount < 5  -> "$amount дня"
            else        -> "$amount дней"
        }
    };

    abstract fun plural(amount: Long): String
}