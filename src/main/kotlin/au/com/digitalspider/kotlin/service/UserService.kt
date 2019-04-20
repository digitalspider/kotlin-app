package au.com.digitalspider.kotlin.controller

import au.com.digitalspider.kotlin.model.User
import au.com.digitalspider.kotlin.repo.UserRepository
import org.apache.commons.lang3.StringUtils
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.*
import javax.validation.Valid

@Service
class UserService(private val userRepository: UserRepository) {

	fun init(user: User): User {
		initRoles(user)
		return user;
	}

	fun initRoles(user: User): User {
		return user;
	}

    fun search(searchTerm: String): List<User> {
        var users = userRepository.findAll();
		return users.filter { user ->
			StringUtils.containsIgnoreCase(user.email, searchTerm) ||
			StringUtils.containsIgnoreCase(user.username, searchTerm)
		}.map { user -> init(user) }
	}
	
    fun getAll(): List<User> =
			userRepository.findAll().map { user -> init(user) }
	
    fun findByUsername(username: String): User {
        try {
			return init(userRepository.findOneByUsernameIgnoreCase(username))
		} catch(e: EmptyResultDataAccessException) {
        	throw IllegalArgumentException("User with username=${username} does not exist")	
		}
	}
	
    fun findByEmail(email: String): User {
		try {
			return init(userRepository.findOneByEmailIgnoreCase(email))
		} catch(e: EmptyResultDataAccessException) {
        	throw IllegalArgumentException("User with email=${email} does not exist")	
		}
	}

    fun create(user: User): User {
		try {
			try {
				findByUsername(user.username);
				throw Exception("User with username ${user.username} already exists");
			} catch(e: IllegalArgumentException) {
				// Ignore: user with username does not exist
			}

			try {
				findByEmail(user.email);
				throw Exception("User with email ${user.email} already exists");
			} catch(e: IllegalArgumentException) {
				// Ignore: user with email does not exist
			}
		} catch(e: Exception) {
			throw IllegalArgumentException(e.message);
		}
		return init(userRepository.save(user));
	}


    fun findById(userId: Long): User {
        return userRepository.findById(userId).map { user ->
			init(user)
		}.orElseThrow{
			IllegalArgumentException("User with id=${userId} does not exist")	
		}
    }

    fun update(userId: Long, newUser: User): User {
        return userRepository.findById(userId).map { existingUser ->
            val updatedUser: User = existingUser.copy(
				username = newUser.username,
            	email = newUser.email
            )
            init(userRepository.save(updatedUser))
		}.orElseThrow{
			IllegalArgumentException("User with id=${userId} does not exist")	
		}
    }

    fun delete(userId: Long) {
		userRepository.findById(userId).map { user  ->
        	userRepository.delete(user)
		}.orElseThrow{
			IllegalArgumentException("User with id=${userId} does not exist")	
		}
    }
}
