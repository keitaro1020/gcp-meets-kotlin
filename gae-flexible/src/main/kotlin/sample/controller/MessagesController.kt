package sample.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sample.model.Message
import sample.repository.MessagesRepository

@RestController
@RequestMapping("/messages")
class MessagesController(
        private val messageRepository: MessagesRepository
) {

    @GetMapping("")
    fun findAll() : ResponseEntity<List<Message>> {
        return ResponseEntity.ok(messageRepository.findAll())
    }

    @GetMapping("/{id}")
    fun find(@PathVariable id: Long) : ResponseEntity<Message> {
        val message = messageRepository.findById(id = id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(message)
    }

    @PostMapping("")
    fun create(@RequestBody message: Message) {
        messageRepository.create(message = message)
    }
}

