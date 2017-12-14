package sample.repository.impl

import com.google.cloud.datastore.Datastore
import com.google.cloud.datastore.Entity
import com.google.cloud.datastore.Key
import com.google.cloud.datastore.KeyFactory
import com.google.cloud.datastore.Query
import org.springframework.stereotype.Repository
import sample.getLong
import sample.getString
import sample.model.Message
import sample.repository.MessagesRepository

@Repository
class DatastoreMessagesRepository(
        private val datastore: Datastore
) : MessagesRepository {

    lateinit var keyFactory: KeyFactory

    private val kind = "Messages"

    init {
        keyFactory = datastore.newKeyFactory().setKind(kind)
    }

    override fun create(message: Message) {
        val key = keyFactory.newKey(message.id)
        datastore.add(messageToEntity(key = key, message = message))
    }

    override fun findAll(): List<Message> {
        val query = Query.newEntityQueryBuilder()
                .setKind(kind)
                .build()
        val results = datastore.run(query)
        return results.asSequence().map { entityToMessage(it) }.toList()
    }

    override fun findById(id: Long): Message? {
        var entity = datastore.get(keyFactory.newKey(id))
        return if(entity != null) { entityToMessage(entity) } else { null }
    }

    private fun messageToEntity(key: Key, message: Message): Entity {
        return Entity.newBuilder(key)
                .set(Message::id.name, message.id)
                .set(Message::text.name, message.text)
                .build()
    }

    private fun entityToMessage(entity: Entity): Message {
        return Message(
                id = entity.getLong(Message::id),
                text = entity.getString(Message::text)
        )
    }
}