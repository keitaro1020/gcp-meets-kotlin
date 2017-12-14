package sample.repository

import sample.model.Message

interface MessagesRepository {

    fun create(message: Message)

    fun findAll(): List<Message>
    fun findById(id: Long): Message?
}