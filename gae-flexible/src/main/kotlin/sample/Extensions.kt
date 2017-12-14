package sample

import com.google.cloud.datastore.Entity
import kotlin.reflect.KProperty

fun Entity.getString(property: KProperty<*>, default: String = ""): String =
        if(this.contains(property.name)){
            this.getString(property.name)
        } else {
            default
        }

fun Entity.getLong(property: KProperty<*>, default: Long = -1): Long =
        if(this.contains(property.name)){
            this.getLong(property.name)
        } else {
            default
        }
