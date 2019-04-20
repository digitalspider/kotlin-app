package au.com.digitalspider.kotlin.repo

import au.com.digitalspider.kotlin.model.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : JpaRepository<Role, Long> {
	
	fun findOneByNameIgnoreCase(name: String): Role
	
	fun findByNameContainsIgnoreCase(name: String): List<Role>
}
