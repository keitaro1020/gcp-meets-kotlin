package sample

import com.google.api.server.spi.config.Api
import com.google.api.server.spi.config.ApiMethod
import com.google.api.server.spi.config.ApiMethod.HttpMethod
import com.google.api.server.spi.config.ApiNamespace
import com.google.api.server.spi.config.Named
import com.google.appengine.api.datastore.DatastoreServiceFactory
import sample.repository.MessagesRepository
import sample.repository.impl.DatastoreMessagesRepository

@Api(
        name = "sample",
        version = "v1",
        namespace =
                ApiNamespace(
                        ownerDomain = "sample",
                        ownerName = "sample",
                        packagePath = ""
                )
)
open class MessagesApi {

    val messageRepository: MessagesRepository

    init {
        messageRepository = DatastoreMessagesRepository(DatastoreServiceFactory.getDatastoreService())
    }

    @ApiMethod(path = "messages/{id}", httpMethod = "get")
    fun getMessage(@Named("id")id: Long) : Message? {
        return messageRepository.findById(id = id)
    }

    @ApiMethod(path = "messages", httpMethod = "get")
    fun getMessages() : List<Message> {
        return messageRepository.findAll()
    }

    @ApiMethod(path = "messages", httpMethod = HttpMethod.POST)
    fun createMessage(message: Message) : Message? {
        messageRepository.create(message = message)
        return messageRepository.findById(id = message.id)
    }

}

data class Message(val id: Long, val text: String)