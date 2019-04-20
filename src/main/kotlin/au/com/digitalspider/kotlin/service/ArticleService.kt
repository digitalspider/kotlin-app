package au.com.digitalspider.kotlin.service

import au.com.digitalspider.kotlin.model.Article
import au.com.digitalspider.kotlin.repo.ArticleRepository
import au.com.digitalspider.kotlin.service.UserService
import org.apache.commons.lang3.StringUtils
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*
import javax.validation.Valid

@Service
class ArticleService(private val articleRepository: ArticleRepository, private val userService: UserService) {

	fun init(article: Article): Article {
		calculateTitleLength(article)
		return article;
	}

	fun calculateTitleLength(article: Article): Article {
		article.titleLength = article.title?.length ?: 0;
		println("article=${article}");
		return article;
	}

    fun search(searchTerm: String): List<Article> {
        var articles = articleRepository.findAll();
		return articles.filter { article ->
			StringUtils.containsIgnoreCase(article.title, searchTerm) ||
			StringUtils.containsIgnoreCase(article.content, searchTerm)
		}.map { article -> init(article) }
	}
	
    fun getAll(): List<Article> =
			articleRepository.findAll().map { article -> init(article) }
	
    fun findByTitle(title: String): List<Article> =
            articleRepository.findByTitleContainingIgnoreCase(title).map { article -> init(article) }

    fun create(article: Article): Article {
		if (article.createdById == 0L) {
			throw IllegalArgumentException("Cannot create article without a createdById value")
		}
		val creator = userService.findById(article.createdById);
		var updatedArticle = article.copy(
			createdBy = creator,
			updatedBy = creator
		);
		try {
			articleRepository.findOneByTitleIgnoreCase(article.title);
			throw IllegalArgumentException("Article with title ${article.title} already exists");
		} catch(e: EmptyResultDataAccessException) {
			// Ignore: article with title does not exist
		}
    	return init(articleRepository.save(updatedArticle))
	}


    fun findById(articleId: Long): Article {
        return articleRepository.findById(articleId).map { article ->
			init(article)
		}.orElseThrow{
			IllegalArgumentException("Article with id=${articleId} does not exist")	
		}
    }

    fun update(articleId: Long, article: Article): Article {
		if (article.updatedById == 0L) {
			throw IllegalArgumentException("Cannot update article without a updatedById value")
		}
        return articleRepository.findById(articleId).map { existingArticle ->
            val updatedArticle: Article = existingArticle.copy(
				title = article.title,
            	content = article.content,
				updatedBy = userService.findById(article.updatedById),
				updatedAt = LocalDateTime.now()
            )
            init(articleRepository.save(updatedArticle))
		}.orElseThrow{
			IllegalArgumentException("Article with id=${articleId} does not exist")	
		}
    }

    fun delete(articleId: Long) {
		articleRepository.findById(articleId).map { existingArticle  ->
            val updatedArticle: Article = existingArticle.copy(
				deletedBy = userService.findById(existingArticle.createdBy!!.id), // TODO: Fix this
            	deletedAt = LocalDateTime.now()
            )
        	articleRepository.save(updatedArticle)
		}.orElseThrow{
			IllegalArgumentException("Article with id=${articleId} does not exist")	
		}
    }
}
