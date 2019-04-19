package au.com.digitalspider.kotlin.controller

import au.com.digitalspider.kotlin.model.Article
import au.com.digitalspider.kotlin.repo.ArticleRepository
import org.apache.commons.lang3.StringUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.*
import javax.validation.Valid

@Service
class ArticleService(private val articleRepository: ArticleRepository) {

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

    fun create(article: Article): Article =
            init(articleRepository.save(article))


    fun findById(articleId: Long): Article {
        return articleRepository.findById(articleId).map { article ->
			init(article)
		}.orElseThrow{
			IllegalArgumentException("Article with id=${articleId} does not exist")	
		}
    }

    fun update(articleId: Long, newArticle: Article): Article {
        return articleRepository.findById(articleId).map { existingArticle ->
            val updatedArticle: Article = existingArticle.copy(
				title = newArticle.title,
            	content = newArticle.content
            )
            init(articleRepository.save(updatedArticle))
		}.orElseThrow{
			IllegalArgumentException("Article with id=${articleId} does not exist")	
		}
    }

    fun delete(articleId: Long) {
		articleRepository.findById(articleId).map { article  ->
        	articleRepository.delete(article)
		}.orElseThrow{
			IllegalArgumentException("Article with id=${articleId} does not exist")	
		}
    }
}
