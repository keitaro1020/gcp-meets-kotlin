package sample

import com.google.appengine.api.datastore.DatastoreService
import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.datastore.EntityNotFoundException
import com.google.appengine.api.datastore.Key
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KProperty

fun Entity.getString(property: KProperty<*>, default: String = ""): String {
    return this.getProperty(property.name) as? String ?: default
}

fun Entity.getLong(property: KProperty<*>, default: Long = -1): Long {
    return this.getProperty(property.name) as? Long ?: default
}

fun DatastoreService.get(key: Key): Entity? =
    try {
        this.get(key)
    } catch (e: EntityNotFoundException) {
        null
    }
