package au.com.digitalspider.kotlin.repo

import au.com.digitalspider.kotlin.model.Article
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ArticleRepository : JpaRepository<Article, Long> {
	
	fun findByTitleContainingIgnoreCase(title: String): List<Article>

	fun findOneByTitleIgnoreCase(title: String): Article
}
