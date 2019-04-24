package au.com.digitalspider.kotlin.auth

import au.com.digitalspider.kotlin.model.User
import au.com.digitalspider.kotlin.model.Role
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.apache.log4j.Logger
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.*
import java.util.concurrent.TimeUnit
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.collections.HashMap

internal object JWTUtils {
    private val expiration: Long = 1L // 1 hour
    private val secret = "Kotlin" // TODO: Change this!
    private val header = "Authorization"
	private val url = "http://localhost:8080";

    private val logger = Logger.getLogger(JWTUtils::class.java)

    fun User.createJwt(): String {
        val claims = HashMap<String, Any>()
        claims.put("roles", this.roles.map{it.name})
        val token = Jwts.builder()
                .setClaims(claims)
                .setSubject(this.username)
                .setExpiration(Date(Date().time + TimeUnit.HOURS.toMillis(expiration)))
                .signWith(SignatureAlgorithm.HS256, secret).compact()
		println("new token=${token}");
		return token;
    }

    fun addAuthentication(response: HttpServletResponse, user: User) {
        val jwt = user.createJwt()
        response.setHeader("Access-Control-Allow-Origin", url)
        response.writer.write(jwt)
        response.writer.flush()
        response.writer.close()
    }

    fun getAuthentication(request: HttpServletRequest): Authentication? {
        var token = request.getHeader(header) ?: return null
		if (token.startsWith("Bearer ")) {
			token = token.substring(7);
		}
		println("token=${token}")

        val tokenBody = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .body

		println("tokenBody=${tokenBody}")
        val username: String = tokenBody.subject
        @Suppress("UNCHECKED_CAST")
        val roles = tokenBody["roles"] as List<String>

        val res = roles.mapTo(LinkedList<GrantedAuthority>()) { SimpleGrantedAuthority(it) }

        logger.info(username + " logged in with authorities " + res)
		println(username + " logged in with authorities " + res)
        return UsernamePasswordAuthenticationToken(username, null, res)
    }
}