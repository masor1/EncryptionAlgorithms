package com.masorone.encryptionalgorithms

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface MessageRepository {

    fun messages(): Flow<List<Message>>

    fun add(message: Message)

    class Base(
        private val listOfMessages: MutableList<Message>
    ) : MessageRepository {

        private var flow = flow {
            emit(listOfMessages)
        }

        override fun messages(): Flow<List<Message>> = flow

        override fun add(message: Message) {
            listOfMessages.add(message)
            flow = flow {
                emit(listOfMessages)
            }
        }
    }
}