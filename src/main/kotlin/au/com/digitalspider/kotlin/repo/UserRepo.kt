package au.com.digitalspider.kotlin.repo

import au.com.digitalspider.kotlin.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
	
	fun findOneByUsernameIgnoreCase(username: String): User
	
	fun findOneByEmailIgnoreCase(email: String): User
}
