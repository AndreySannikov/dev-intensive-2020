package ru.skillbranch.devintensive.models

import ru.skillbranch.devintensive.extensions.humanizeDiff
import java.util.*

abstract class BaseMessage(
    val id: String,
    val from: User?,
    val chat: Chat,
    val isIncoming: Boolean = false,
    val date: Date = Date()
) {
    abstract fun formatMessage(): String

    protected fun doFormatMessage(msg: String) =
        "${from?.firstName} ${if (isIncoming) "получил" else "отправил" } $msg ${date.humanizeDiff()}"

    companion object AbstractFactory {
        var id = 0

        fun makeMessage(from: User?, chat: Chat, date: Date = Date(), type: String, payload: Any, isIncoming: Boolean = false): BaseMessage =
            when (type) {
                "image" -> ImageMessage("${id++}", from, chat, isIncoming, date, payload as String)
                else -> TextMessage("${id++}", from, chat, date = date, text = payload as String)
            }
    }
}