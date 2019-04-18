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

	fun initArticle(article: Article): Article {
		calculateTitleLength(article)
		return article;
	}

	fun calculateTitleLength(article: Article): Article {
		article.titleLength = article.title?.length ?: 0;
		println("article=${article}");
		return article;
	}

    fun getAllArticles(searchTerm: String?): List<Article> {
        var articles = articleRepository.findAll();
		if (StringUtils.isNotBlank(searchTerm)) {
			articles = articles.filter { article ->
				StringUtils.containsIgnoreCase(article.title, searchTerm) ||
				StringUtils.containsIgnoreCase(article.content, searchTerm)
			}
		}
		return articles.map { article -> initArticle(article) }
	}
	
    fun getAllArticles(): List<Article> =
			articleRepository.findAll().map { article -> initArticle(article) }
	
    fun getAllArticlesByTitle(title: String): List<Article> =
            articleRepository.findByTitleContainingIgnoreCase(title).map { article -> initArticle(article) }

    fun createNewArticle(article: Article): Article =
            initArticle(articleRepository.save(article))


    fun findById(articleId: Long): Article {
        return articleRepository.findById(articleId).map { article ->
			initArticle(article)
		}.orElseThrow{
			IllegalArgumentException("Article with id=${articleId} does not exist")	
		}
    }

    fun updateArticleById(articleId: Long, newArticle: Article): Article {
        return articleRepository.findById(articleId).map { existingArticle ->
            val updatedArticle: Article = existingArticle.copy(
				title = newArticle.title,
            	content = newArticle.content
            )
            initArticle(articleRepository.save(updatedArticle))
		}.orElseThrow{
			IllegalArgumentException("Article with id=${articleId} does not exist")	
		}
    }

    fun deleteArticleById(articleId: Long) {
		articleRepository.findById(articleId).map { article  ->
        	articleRepository.delete(article)
		}.orElseThrow{
			IllegalArgumentException("Article with id=${articleId} does not exist")	
		}
    }
}
