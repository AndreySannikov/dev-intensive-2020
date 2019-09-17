package ru.skillbranch.devintensive.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.extensions.mutableLiveData
import ru.skillbranch.devintensive.models.data.Chat
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.repositories.ChatRepository

class MainViewModel : ViewModel() {
    private val chatRepository = ChatRepository
    private val query = mutableLiveData("")
    private val chats = Transformations.map(chatRepository.loadChats()) { chats ->
        chats
            .groupBy { it.isArchived }
    }

    fun getActiveChatData(): LiveData<List<ChatItem>> {
        val result = MediatorLiveData<List<ChatItem>>()

        val filterF = {
            val archiveChatItem = chats.value!![true]?.let { Chat.toArchiveChatItem(it) }

            val activeChatItems = chats.value!![false]
                ?.map { it.toChatItem() }
                ?.sortedBy { it.id.toInt() }
                ?: emptyList()
            val q = query.value!!
            val filtered =
                if (q.isEmpty()) activeChatItems else activeChatItems.filter {
                    it.title.contains(
                        q,
                        true
                    )
                }

            result.value = if (archiveChatItem == null) {
                filtered
            } else {
                listOf(archiveChatItem).plus(filtered)
            }
        }

        result.addSource(chats) { filterF.invoke() }
        result.addSource(query) { filterF.invoke() }
        return result
    }

    fun getArchiveChatData(): LiveData<List<ChatItem>> {
        val result = MediatorLiveData<List<ChatItem>>()

        val filterF = {
            val activeChatItems = chats.value!![true]
                ?.map { it.toChatItem() }
                ?.sortedBy { it.id.toInt() }
                ?: emptyList()
            val q = query.value!!

            result.value =
                if (q.isEmpty()) activeChatItems else activeChatItems.filter {
                    it.title.contains(
                        q,
                        true
                    )
                }
        }

        result.addSource(chats) { filterF.invoke() }
        result.addSource(query) { filterF.invoke() }
        return result
    }

    fun addToArchive(chatId: String) {
        chatRepository.find(chatId)?.apply {
            chatRepository.update(this.copy(isArchived = true))
        }
    }

    fun restoreFromArchive(chatId: String) {
        chatRepository.find(chatId)?.apply {
            chatRepository.update(this.copy(isArchived = false))
        }
    }

    fun handleSearchQuery(text: String?) {
        query.value = text
    }

}