package au.com.digitalspider.kotlin.service

import au.com.digitalspider.kotlin.model.User
import au.com.digitalspider.kotlin.repo.UserRepository
import au.com.digitalspider.kotlin.auth.SecurityUserDetails
import java.time.LocalDateTime
import java.util.*
import javax.validation.Valid
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) : UserDetailsService {

	fun init(user: User): User {
		initRoles(user)
		return user;
	}

	fun initRoles(user: User): User {
		return user;
	}
	
    override fun loadUserByUsername(username: String): UserDetails {
		try {
			println("username=${username}");
	        val user: User = findByUsername(username)
			println("user=${user}");
	        val securityUserDetails = SecurityUserDetails(user)
			println("securityUserDetails=${securityUserDetails}");
			return securityUserDetails;
		} catch (e: IllegalArgumentException) {
			throw UsernameNotFoundException(e.message)
		}
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
			userRepository.findOneByUsernameIgnoreCase(user.username)
			throw IllegalArgumentException("User with username ${user.username} already exists");
		} catch(e: EmptyResultDataAccessException) {
			// Ignore: user with username does not exist
		}
		try {
			userRepository.findOneByEmailIgnoreCase(user.email)
			throw IllegalArgumentException("User with email ${user.email} already exists");
		} catch(e: EmptyResultDataAccessException) {
			// Ignore: user with email does not exist
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

    fun update(userId: Long, user: User): User {
        return userRepository.findById(userId).map { existingUser ->
            val updatedUser: User = existingUser.copy(
				username = user.username,
            	email = user.email,
				roles = user.roles
            )
            init(userRepository.save(updatedUser))
		}.orElseThrow{
			IllegalArgumentException("User with id=${userId} does not exist")	
		}
    }

    fun delete(userId: Long) {
		userRepository.findById(userId).map { existingUser  ->
            val updatedUser = existingUser.copy(
            	deletedAt = LocalDateTime.now()
            )
        	userRepository.save(updatedUser)
		}.orElseThrow{
			IllegalArgumentException("User with id=${userId} does not exist")	
		}
    }
}
