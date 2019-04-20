package au.com.digitalspider.kotlin.model

/*
import org.hibernate.annotations.Filter
import org.hibernate.annotations.FilterDef
import org.hibernate.annotations.Loader
import org.hibernate.annotations.NamedNativeQuery
import org.hibernate.annotations.ParamDef
*/
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.validation.constraints.NotBlank

@Entity
// @Loader(namedQuery = "activeSelect")
// @NamedNativeQuery(name="activeSelect", query="select * from article where (deleted_at is null or deleted_at>now()) and id= ?", resultClass = Article.class)
data class Article (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @get: NotBlank
    val title: String = "",

    @get: NotBlank
    val content: String = "",

    @Transient var titleLength: Int = 0,

    @Transient var createdById: Long = 0,
    @Transient var updatedById: Long = 0,
    @Transient var deletedById: Long = 0,

	@ManyToOne
    @JoinColumn(insertable = true, updatable = false)
    val createdBy: User? = null,

    @Column(insertable = true, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

	@ManyToOne
    val updatedBy: User? = null,

    val updatedAt: LocalDateTime = LocalDateTime.now(),

	@ManyToOne
    val deletedBy: User? = null,

    val deletedAt: LocalDateTime? = null

)
