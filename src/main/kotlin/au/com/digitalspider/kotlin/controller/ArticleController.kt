package au.com.digitalspider.kotlin.controller

import au.com.digitalspider.kotlin.model.Article
import au.com.digitalspider.kotlin.repo.ArticleRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/api")
class ArticleController(private val articleService: ArticleService) {
	
    @GetMapping("/articles")
    fun getAllArticles(): List<Article> =
            articleService.getAllArticles(null)
	
    @GetMapping("/articles/title/{title}")
    fun getAllArticlesByTitle(@PathVariable(value = "title") title: String): Iterable<Article> =
            articleService.getAllArticlesByTitle(title)
	
    @GetMapping("/articles/title/length/{titleLength}")
    fun getAllArticlesByTitle(@PathVariable(value = "titleLength") titleLength: Int): Iterable<Article> =
            articleService.getAllArticles(null).filter { article -> article.titleLength>=titleLength }
	
    @GetMapping("/articles/search/{searchTerm}")
    fun getAllArticlesAndSearch(@PathVariable(value = "searchTerm") searchTerm: String): Iterable<Article> =
            articleService.getAllArticles(searchTerm)

    @PostMapping("/articles")
    fun createNewArticle(@Valid @RequestBody article: Article): Article =
            articleService.createNewArticle(article)


    @GetMapping("/articles/{id}")
    fun getArticleById(@PathVariable(value = "id") articleId: Long): ResponseEntity<Article> {
        try {
        	val article = articleService.findById(articleId); 
            return ResponseEntity.ok(article)
        } catch(e: IllegalArgumentException) {
        	return ResponseEntity.notFound().build()
		}
    }

    @PutMapping("/articles/{id}")
    fun updateArticleById(@PathVariable(value = "id") articleId: Long,
                          @Valid @RequestBody newArticle: Article): ResponseEntity<Article> {
		try {
	        val updatedArticle = articleService.updateArticleById(articleId, newArticle)
	        return ResponseEntity.ok().body(updatedArticle)
        } catch(e: IllegalArgumentException) {
        	return ResponseEntity.notFound().build()
		}
    }

    @DeleteMapping("/articles/{id}")
    fun deleteArticleById(@PathVariable(value = "id") articleId: Long): ResponseEntity<Void> {
        try {
            articleService.deleteArticleById(articleId)
            return ResponseEntity<Void>(HttpStatus.OK)
        } catch(e: IllegalArgumentException) {
        	return ResponseEntity.notFound().build()
		}
    }
}
