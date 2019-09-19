package ru.skillbranch.devintensive.models.data

import androidx.annotation.VisibleForTesting
import ru.skillbranch.devintensive.App
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.data.managers.CacheManager
import ru.skillbranch.devintensive.extensions.shortFormat
import ru.skillbranch.devintensive.models.BaseMessage
import ru.skillbranch.devintensive.models.ImageMessage
import ru.skillbranch.devintensive.models.TextMessage
import ru.skillbranch.devintensive.utils.Utils
import java.util.*

data class Chat(
    val id: String,
    val title: String,
    val members: List<User> = listOf(),
    var messages: MutableList<BaseMessage> = mutableListOf(),
    var isArchived: Boolean = false
) {
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun unreadableMessageCount(): Int =
        messages.count { !it.isReaded }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun lastMessageDate(): Date? = messages.lastOrNull()?.date

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun lastMessageShort(): Pair<String, String?> = when (val lastMessage = messages.lastOrNull()) {
        is TextMessage -> (lastMessage.text
            ?: "Сообщений пока что нет") to lastMessage.from.firstName
        is ImageMessage -> ("${lastMessage.from.firstName} - отправил фото") to lastMessage.from.firstName
        else -> noMessages
    }

    private fun isSingle(): Boolean = members.size == 1

    fun toChatItem(): ChatItem {
        val (lastMessage, lastAuthor) = lastMessageShort()

        return if (isSingle()) {
            val user = members.first()
            ChatItem(
                id,
                user.avatar,
                Utils.toInitials(user.firstName, user.lastName) ?: "??",
                "${user.firstName ?: ""} ${user.lastName ?: ""}",
                lastMessage,
                unreadableMessageCount(),
                lastMessageDate()?.shortFormat(),
                user.isOnline
            )
        } else {
            ChatItem(
                id,
                null,
                "",
                title,
                lastMessage,
                unreadableMessageCount(),
                lastMessageDate()?.shortFormat(),
                false,
                ChatType.GROUP,
                lastAuthor
            )
        }
    }

    companion object {
        private val noMessages = "Сообщений пока что нет" to null

        fun toArchiveChatItem(archiveChats: List<Chat>): ChatItem {
            val chatsWithMessages = archiveChats.filter { it.messages.isNotEmpty() }
            val chatWithLastMessage = chatsWithMessages.maxBy { it.lastMessageDate()!! }
            val t = chatWithLastMessage?.lastMessageShort() ?: noMessages

            return ChatItem(
                CacheManager.nextChatId(),
                null,
                "",
                App.applicationContext().getString(R.string.archive_chats_title),
                t.first,
                chatsWithMessages.sumBy { it.unreadableMessageCount() },
                chatWithLastMessage?.lastMessageDate()?.shortFormat(),
                chatType = ChatType.ARCHIVE,
                author = t.second?.let { "@$it" }
            )
        }
    }

}

enum class ChatType {
    SINGLE,
    GROUP,
    ARCHIVE
}



