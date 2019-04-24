package au.com.digitalspider.kotlin.config

import au.com.digitalspider.kotlin.auth.JWTAuthenticationFilter
import au.com.digitalspider.kotlin.auth.JWTLoginFilter
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.session.SessionManagementFilter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.crypto.password.NoOpPasswordEncoder

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity
//@Order(SecurityProperties.DEFAULT_FILTER_ORDER)
class SecurityConfig : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        //http.cors().and().
        http.
                csrf()
                    .disable()
                .antMatcher("/**").authorizeRequests()
                    .antMatchers("/user/**", "/browser/**", "/public/**").permitAll()
                    .anyRequest().authenticated()
                    .antMatchers("/metrics").hasAuthority("ADMIN")
                .and()
                .addFilterBefore(CorsFilter(), SessionManagementFilter::class.java)
                    .addFilterBefore(JWTLoginFilter("/user/login", authenticationManager()),
                        UsernamePasswordAuthenticationFilter::class.java)
                    .addFilterBefore(JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }

	@Bean
	fun passwordEncoder() : PasswordEncoder {
		return NoOpPasswordEncoder.getInstance();
	}

//	fun getPasswordEncoder() : PasswordEncoder {
//		val encodingStrength = 16;
//		val encoder : PasswordEncoder = BCryptPasswordEncoder(encodingStrength);
//		println(encoder.encode("test123"));
//		return encoder;
//	}
}
