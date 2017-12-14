package sample

import com.google.cloud.datastore.Datastore
import com.google.cloud.datastore.DatastoreOptions
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class Application {
    @Bean
    fun cloudDatastoreService(): Datastore {
        return DatastoreOptions.getDefaultInstance().service
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
