package ru.skillbranch.devintensive.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.extensions.mutableLiveData
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.repositories.ChatRepository

class ArchiveViewModel : ViewModel() {
    private val chatRepository = ChatRepository
    private val query = mutableLiveData("")
    private val chats = Transformations.map(chatRepository.loadChats()) { chats ->
        chats
            .filter { it.isArchived }
            .map { it.toChatItem() }
            .sortedBy { it.id.toInt() }
    }

    fun getChatData(): LiveData<List<ChatItem>> {
        val result = MediatorLiveData<List<ChatItem>>()

        val filterF = {
            val q = query.value!!
            result.value =
                if (q.isEmpty()) chats.value!! else chats.value!!.filter {
                    it.title.contains(q, true)
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