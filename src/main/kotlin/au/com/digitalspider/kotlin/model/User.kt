package au.com.digitalspider.kotlin.model

import au.com.digitalspider.kotlin.annotation.ValidEmail
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity
data class User (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @get: NotBlank
    val username: String = "",

    @get: NotBlank
    val password: String = "",
	
    @get: NotBlank
	@ValidEmail
	@Size(min = 3, max = 64, message = "length must be 3 to 64")
    val email: String = ""

    // val roles: List<Role>
)
