package au.com.digitalspider.kotlin.model

import au.com.digitalspider.kotlin.annotation.ValidEmail
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.OneToMany
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity
data class User (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @get: NotBlank
    val username: String = "",

    val password: String = "",
	
    @get: NotBlank
	@ValidEmail
	@Size(min = 3, max = 64, message = "length must be 3 to 64")
    val email: String = "",

	@OneToMany(cascade = arrayOf(CascadeType.ALL), fetch = FetchType.EAGER)
	@JoinTable(name = "userroles", joinColumns = arrayOf(JoinColumn(name = "user_id")), inverseJoinColumns = arrayOf(JoinColumn(name = "role_id")))
    val roles: List<Role> = ArrayList<Role>()
)
