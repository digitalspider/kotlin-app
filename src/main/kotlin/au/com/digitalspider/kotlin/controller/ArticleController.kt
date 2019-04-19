package au.com.digitalspider.kotlin.controller

import au.com.digitalspider.kotlin.model.Article
import au.com.digitalspider.kotlin.repo.ArticleRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/article")
class ArticleController(private val articleService: ArticleService) {
	
    @GetMapping("")
    fun getAll(): List<Article> =
            articleService.getAll()
	
    @GetMapping("/title/{title}")
    fun findByTitle(@PathVariable(value = "title") title: String): Iterable<Article> =
            articleService.findByTitle(title)
	
    @GetMapping("/title/length/{titleLength}")
    fun findByTitleLength(@PathVariable(value = "titleLength") titleLength: Int): Iterable<Article> =
            articleService.getAll().filter { article -> article.titleLength>=titleLength }
	
    @GetMapping("/search/{searchTerm}")
    fun search(@PathVariable(value = "searchTerm") searchTerm: String): Iterable<Article> =
            articleService.search(searchTerm)

    @PostMapping("")
    fun create(@Valid @RequestBody article: Article): Article =
            articleService.create(article)


    @GetMapping("/{id}")
    fun get(@PathVariable(value = "id") articleId: Long): ResponseEntity<Article> {
        try {
        	val article = articleService.findById(articleId); 
            return ResponseEntity.ok(article)
        } catch(e: IllegalArgumentException) {
        	return ResponseEntity.notFound().build()
		}
    }

    @PutMapping("/{id}")
    fun update(@PathVariable(value = "id") articleId: Long,
                          @Valid @RequestBody newArticle: Article): ResponseEntity<Article> {
		try {
	        val updatedArticle = articleService.update(articleId, newArticle)
	        return ResponseEntity.ok().body(updatedArticle)
        } catch(e: IllegalArgumentException) {
        	return ResponseEntity.notFound().build()
		}
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable(value = "id") articleId: Long): ResponseEntity<Void> {
        try {
            articleService.delete(articleId)
            return ResponseEntity<Void>(HttpStatus.OK)
        } catch(e: IllegalArgumentException) {
        	return ResponseEntity.notFound().build()
		}
    }
}
