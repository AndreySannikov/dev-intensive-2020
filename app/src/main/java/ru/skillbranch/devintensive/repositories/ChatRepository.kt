package ru.skillbranch.devintensive.repositories

import androidx.lifecycle.MutableLiveData
import ru.skillbranch.devintensive.data.managers.CacheManager
import ru.skillbranch.devintensive.models.data.Chat

object ChatRepository {
    private val chats = CacheManager.loadChats()

    fun loadChats(): MutableLiveData<List<Chat>> {
        return chats
    }

    fun find(chatId: String): Chat? = chats.value!!.first { it.id == chatId }

    fun update(chat: Chat) {
        val copy = chats.value!!.toMutableList()

        copy.indexOfFirst { it.id == chat.id }.also {
            if (it != -1) {
                copy[it] = chat
                chats.value = copy
            }
        }
    }
}