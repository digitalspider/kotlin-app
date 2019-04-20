package au.com.digitalspider.kotlin.controller

import au.com.digitalspider.kotlin.model.User
import au.com.digitalspider.kotlin.io.Error
import au.com.digitalspider.kotlin.repo.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/user")
class UserController(private val userService: UserService) {
	
    @GetMapping("")
    fun getAll(): List<User> =
            userService.getAll()
	
    @GetMapping("/username/{username}")
    fun findByUsername(@PathVariable(value = "username") username: String): ResponseEntity<User> {
		try {
            return ResponseEntity.ok(userService.findByUsername(username))
        } catch(e: IllegalArgumentException) {
        	return ResponseEntity.notFound().build()
		}
	}
	
    @GetMapping("/email/{email}")
    fun findByEmail(@PathVariable(value = "email") email: String): ResponseEntity<User> {
		try {
            return ResponseEntity.ok(userService.findByEmail(email))
        } catch(e: IllegalArgumentException) {
        	return ResponseEntity.notFound().build()
		}
	}
	
    @GetMapping("/search/{searchTerm}")
    fun search(@PathVariable(value = "searchTerm") searchTerm: String): Iterable<User> =
            userService.search(searchTerm)

    @PostMapping("")
    fun create(@Valid @RequestBody user: User): ResponseEntity<Any> {
        try {
        	val user = userService.create(user);
            return ResponseEntity.ok(user)
        } catch(e: IllegalArgumentException) {
            val status = HttpStatus.PRECONDITION_FAILED;
            val error = Error(status.value(), e.message);
        	return ResponseEntity.status(status).body(error)
		}
    }

    @GetMapping("/{id}")
    fun get(@PathVariable(value = "id") userId: Long): ResponseEntity<User> {
        try {
        	val user = userService.findById(userId);
            return ResponseEntity.ok(user)
        } catch(e: IllegalArgumentException) {
        	return ResponseEntity.notFound().build()
		}
    }

    @PutMapping("/{id}")
    fun update(@PathVariable(value = "id") userId: Long,
                          @Valid @RequestBody newUser: User): ResponseEntity<User> {
		try {
	        val updatedUser = userService.update(userId, newUser)
	        return ResponseEntity.ok().body(updatedUser)
        } catch(e: IllegalArgumentException) {
        	return ResponseEntity.notFound().build()
		}
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable(value = "id") userId: Long): ResponseEntity<Void> {
        try {
            userService.delete(userId)
            return ResponseEntity<Void>(HttpStatus.OK)
        } catch(e: IllegalArgumentException) {
        	return ResponseEntity.notFound().build()
		}
    }
}
