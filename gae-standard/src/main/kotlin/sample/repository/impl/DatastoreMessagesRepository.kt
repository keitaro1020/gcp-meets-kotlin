package sample.repository.impl

import com.google.appengine.api.datastore.DatastoreService
import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.datastore.Key
import com.google.appengine.api.datastore.KeyFactory
import com.google.appengine.api.datastore.Query
import org.springframework.stereotype.Repository
import sample.getLong
import sample.getString
import sample.model.Message
import sample.repository.MessagesRepository

@Repository
class DatastoreMessagesRepository(
        private val datastore: DatastoreService
) : MessagesRepository {

    private val kind = "Messages"

    override fun create(message: Message) {
        datastore.put(messageToEntity(message = message))
    }

    override fun findAll(): List<Message> {
        val query = Query(kind)
        query.addSort(Message::id.name, Query.SortDirection.ASCENDING)

        val prepare = datastore.prepare(query)
        val results = prepare.asQueryResultIterator()

        return results.asSequence().map { entityToMessage(it) }.toList()
    }

    override fun findById(id: Long): Message? {
        val key = KeyFactory.createKey(kind, id)
        val entity = datastore.get(key)
        return if(entity != null) { entityToMessage(entity) } else { null }
    }

    private fun messageToEntity(key: Key? = null, message: Message): Entity {
        val entity = if(key != null) Entity(key) else Entity(kind, message.id)
        entity.let {
            it.setProperty(Message::id.name, message.id)
            it.setProperty(Message::text.name, message.text)
        }
        return entity
    }

    private fun entityToMessage(entity: Entity): Message {
        return Message(
                id = entity.getLong(Message::id),
                text = entity.getString(Message::text)
        )
    }
}